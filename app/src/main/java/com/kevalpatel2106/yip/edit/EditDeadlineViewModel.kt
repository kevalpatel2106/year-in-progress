package com.kevalpatel2106.yip.edit

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
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
import com.kevalpatel2106.yip.entity.getDeadlineColor
import com.kevalpatel2106.yip.entity.isPreBuild
import com.kevalpatel2106.yip.notifications.DeadlineNotificationReceiver
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import com.kevalpatel2106.yip.repo.utils.validator.Validator
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.Date
import java.util.concurrent.TimeUnit

internal class EditDeadlineViewModel @ViewModelInject internal constructor(
    @ApplicationContext private val application: Context,
    private val deadlineRepo: DeadlineRepo,
    private val alarmRepo: AlarmRepo,
    private val validator: Validator,
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    private val titleLength by lazy { application.resources.getInteger(R.integer.max_process_title) }
    private var deadlineId: Long = 0L

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

    internal fun setDeadlineId(deadlineId: Long) {
        if (this.deadlineId != deadlineId) {
            monitorDeadline(deadlineId)
            this.deadlineId = deadlineId
        }
    }

    private fun monitorDeadline(id: Long) {
        deadlineRepo.observeDeadline(id)
            .firstOrError()
            .doOnSubscribe { _viewState.value = _viewState.value?.copy(isLoading = true) }
            .subscribe({ deadline ->

                _viewState.value = _viewState.value?.copy(
                    isLoading = false,
                    isSomethingChanged = false,

                    type = deadline.deadlineType,

                    initialTitle = deadline.title,
                    currentTitle = deadline.title,
                    titleErrorMsg = null,

                    allowEditDate = !deadline.deadlineType.isPreBuild(),
                    startTime = deadline.start,
                    endTime = deadline.end,

                    selectedColor = deadline.color,

                    notificationList = deadline.notificationPercent
                )
            }, {
                Timber.e(it)
                _viewState.value = _viewState.value?.copy(isLoading = false)
                _userMessage.value = it.message
                _closeSignal.value = true
            })
            .addTo(compositeDisposable)
    }

    internal fun onStartDateSelected(startDate: Date) {
        val endDate =
            if (!validator.isValidStartDate(startDate, _viewState.value?.endTime)) {
                Date(startDate.time + TimeUnit.DAYS.toMillis(1))
            } else {
                _viewState.value?.endTime
                    ?: throw IllegalStateException("End date is null.")
            }

        _viewState.value = _viewState.value?.copy(
            isSomethingChanged = true,
            startTime = startDate,
            endTime = endDate
        )
    }

    internal fun onEndDateSelected(endDate: Date) {
        if (validator.isValidEndDate(_viewState.value?.startTime, endDate)) {
            _viewState.value = _viewState.value?.copy(
                isSomethingChanged = true,
                endTime = endDate
            )
        } else {
            _userMessage.value = application.getString(R.string.error_start_date_after_end_date)
            _viewState.recall()
        }
    }

    internal fun onColorSelected(@ColorInt color: Int) {
        if (validator.isValidDeadlineColor(color)) {
            _viewState.value = _viewState.value?.copy(
                isSomethingChanged = true,
                selectedColor = getDeadlineColor(color)
            )
        } else {
            _userMessage.value = application.getString(R.string.error_invalid_deadline)
            _viewState.recall()
        }
    }

    internal fun onTitleChanged(title: String) {
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
                        R.string.error_deadline_title_long,
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
    internal fun saveDeadline() {
        _viewState.value?.run {
            if (isLoading) {
                return
            }

            if (!validator.isValidTitle(currentTitle)) {
                _viewState.value?.copy(
                    titleErrorMsg = application.getString(
                        R.string.error_deadline_title_long,
                        titleLength
                    )
                )
                return
            }

            if (!validator.isValidStartDate(startTime, endTime)) {
                _userMessage.value = application.getString(R.string.error_start_date_after_end_date)
                return
            }

            if (!validator.isValidEndDate(startTime, endTime)) {
                _userMessage.value = application.getString(R.string.error_start_date_after_end_date)
                return
            }

            if (!validator.isValidDeadlineColor(selectedColor.colorInt)) {
                _userMessage.value = application.getString(R.string.error_invalid_deadline)
                return
            }

            if (!validator.isValidNotification(notificationList)) {
                _userMessage.value = application.getString(R.string.invalid_notification_percent)
                return
            }

            deadlineRepo.addUpdateDeadline(
                deadlineId = deadlineId,
                title = currentTitle.capitalize(),
                startTime = startTime.apply { setToDayMin() },
                endTime = endTime.apply { setToDayMax() },
                color = selectedColor,
                type = type,
                notifications = notificationList
            ).doOnSubscribe {
                _viewState.value = _viewState.value?.copy(isLoading = true)
            }.doOnSuccess {
                _userMessage.value = application.getString(R.string.deadline_saved_success)
            }.delay(SNACK_BAR_DURATION, TimeUnit.MILLISECONDS, RxSchedulers.main)
                .doAfterTerminate {
                    _viewState.value = _viewState.value?.copy(isLoading = false)
                }.subscribe({
                    alarmRepo.updateAlarms(DeadlineNotificationReceiver::class.java)
                    _closeSignal.value = true
                }, {
                    Timber.e(it)
                    _userMessage.value = it.message
                })
                .addTo(compositeDisposable)
        }
    }
}
