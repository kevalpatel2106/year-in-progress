package com.kevalpatel2106.yip.edit

import android.app.Application
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.SNACK_BAR_DURATION
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.livedata.SingleLiveEvent
import com.kevalpatel2106.yip.core.livedata.recall
import com.kevalpatel2106.yip.core.setToDayMax
import com.kevalpatel2106.yip.core.setToDayMin
import com.kevalpatel2106.yip.entity.getProgressColor
import com.kevalpatel2106.yip.entity.isPreBuild
import com.kevalpatel2106.yip.notifications.ProgressNotificationReceiver
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.progressesRepo.ProgressRepo
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import com.kevalpatel2106.yip.repo.utils.Validator
import timber.log.Timber
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class EditViewProgressModel @Inject internal constructor(
    private val application: Application,
    private val progressRepo: ProgressRepo,
    private val alarmRepo: AlarmRepo,
    private val validator: Validator,
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    private val titleLength by lazy { application.resources.getInteger(R.integer.max_process_title) }
    private var progressId: Long = 0L

    @VisibleForTesting
    internal val _viewState = MutableLiveData<EditViewState>(EditViewState.initialState())
    internal val viewState: LiveData<EditViewState> = _viewState

    private val _closeSignal = SingleLiveEvent<Boolean>()
    internal val closeSignal: LiveData<Boolean> = _closeSignal

    private val _userMessage = SingleLiveEvent<String>()
    internal val userMessage: LiveData<String> = _userMessage

    init {
        monitorProStatus()
    }

    private fun monitorProStatus() {
        billingRepo.observeIsPurchased()
            .subscribe {
                _viewState.value = _viewState.value?.copy(
                    lockColorPicker = !it,
                    lockNotification = !it
                )
            }
            .addTo(compositeDisposable)
    }

    internal fun setProgressId(progressId: Long) {
        if (this.progressId != progressId) {
            monitorProgress(progressId)
            this.progressId = progressId
        }
    }

    private fun monitorProgress(id: Long) {
        progressRepo.observeProgress(id)
            .firstOrError()
            .doOnSubscribe { _viewState.value = _viewState.value?.copy(isLoadingProgress = true) }
            .subscribe({ progress ->

                _viewState.value = _viewState.value?.copy(
                    isLoadingProgress = false,
                    isSomethingChanged = false,

                    progressType = progress.progressType,

                    initialTitle = progress.title,
                    currentTitle = progress.title,
                    titleErrorMsg = null,

                    allowEditDate = !progress.progressType.isPreBuild(),
                    progressStartTime = progress.start,
                    progressEndTime = progress.end,

                    progressColor = progress.color,

                    notificationList = progress.notificationPercent
                )
            }, {
                Timber.e(it)
                _viewState.value = _viewState.value?.copy(isLoadingProgress = false)
                _userMessage.value = it.message
                _closeSignal.value = true
            })
            .addTo(compositeDisposable)
    }

    internal fun onProgressStartDateSelected(startDate: Date) {
        val endDate =
            if (!validator.isValidStartDate(startDate, _viewState.value?.progressEndTime)) {
                Date(startDate.time + TimeUnit.DAYS.toMillis(1))
            } else {
                _viewState.value?.progressEndTime
                    ?: throw IllegalStateException("End date is null.")
            }

        _viewState.value = _viewState.value?.copy(
            isSomethingChanged = true,
            progressStartTime = startDate,
            progressEndTime = endDate
        )
    }

    internal fun onProgressEndDateSelected(endDate: Date) {
        if (validator.isValidEndDate(_viewState.value?.progressStartTime, endDate)) {
            _viewState.value = _viewState.value?.copy(
                isSomethingChanged = true,
                progressEndTime = endDate
            )
        } else {
            _userMessage.value = application.getString(R.string.error_start_date_after_end_date)
            _viewState.recall()
        }
    }

    internal fun onProgressColorSelected(@ColorInt color: Int) {
        if (validator.isValidProgressColor(color)) {
            _viewState.value = _viewState.value?.copy(
                isSomethingChanged = true,
                progressColor = getProgressColor(color)
            )
        } else {
            _userMessage.value = application.getString(R.string.error_invalid_progress)
            _viewState.recall()
        }
    }

    internal fun onProgressTitleChanged(title: String) {
        when {
            title.trim() == _viewState.value?.initialTitle?.trim() -> {
                return
            }
            validator.isValidTitle(title) -> {
                _viewState.value = _viewState.value?.copy(
                    isSomethingChanged = true,
                    currentTitle = title,
                    titleErrorMsg = null
                )
            }
            else -> {
                _viewState.value = _viewState.value?.copy(
                    titleErrorMsg = application.getString(
                        R.string.error_progress_title_long,
                        titleLength
                    )
                )
            }
        }
    }

    internal fun onNotificationAdded(notificationPercent: Float) {
        val list = _viewState.value?.notificationList?.toMutableList() ?: mutableListOf()
        if (!list.any { it == notificationPercent }) {
            list.add(notificationPercent)
        }
        _viewState.value =
            _viewState.value?.copy(notificationList = list, isSomethingChanged = true)
    }

    internal fun onNotificationRemoved(notificationPercent: Float) {
        val list = _viewState.value?.notificationList?.toMutableList() ?: mutableListOf()
        list.remove(notificationPercent)
        _viewState.value =
            _viewState.value?.copy(notificationList = list, isSomethingChanged = true)
    }

    @SuppressWarnings("ReturnCount", "ComplexMethod")
    internal fun saveProgress() {
        _viewState.value?.run {
            if (isLoadingProgress) {
                return
            }

            if (!validator.isValidTitle(currentTitle)) {
                _viewState.value?.copy(
                    titleErrorMsg = application.getString(
                        R.string.error_progress_title_long,
                        titleLength
                    )
                )
                return
            }

            if (!validator.isValidStartDate(progressStartTime, progressEndTime)) {
                _userMessage.value = application.getString(R.string.error_start_date_after_end_date)
                return
            }

            if (!validator.isValidEndDate(progressStartTime, progressEndTime)) {
                _userMessage.value = application.getString(R.string.error_start_date_after_end_date)
                return
            }

            if (!validator.isValidProgressColor(progressColor.colorInt)) {
                _userMessage.value = application.getString(R.string.error_invalid_progress)
                return
            }

            if (!validator.isValidNotification(notificationList)) {
                _userMessage.value = application.getString(R.string.invalid_notification_percent)
                return
            }

            progressRepo.addUpdateProgress(
                processId = progressId,
                title = currentTitle.capitalize(),
                startTime = progressStartTime.apply { setToDayMin() },
                endTime = progressEndTime.apply { setToDayMax() },
                color = progressColor,
                progressTypeType = progressType,
                notifications = notificationList
            ).doOnSubscribe {
                _viewState.value = _viewState.value?.copy(isLoadingProgress = true)
            }.doOnSuccess {
                _userMessage.value = application.getString(R.string.progress_saved_success)
            }.delay(SNACK_BAR_DURATION, TimeUnit.MILLISECONDS, RxSchedulers.main)
                .doAfterTerminate {
                    _viewState.value = _viewState.value?.copy(isLoadingProgress = false)
                }.subscribe({
                    alarmRepo.updateAlarms(ProgressNotificationReceiver::class.java)
                    _closeSignal.value = true
                }, {
                    Timber.e(it)
                    _userMessage.value = it.message
                })
                .addTo(compositeDisposable)
        }
    }
}
