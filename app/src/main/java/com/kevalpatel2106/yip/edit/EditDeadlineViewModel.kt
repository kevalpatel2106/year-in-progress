package com.kevalpatel2106.yip.edit

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.RxSchedulers
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.livedata.SingleLiveEvent
import com.kevalpatel2106.yip.core.livedata.modify
import com.kevalpatel2106.yip.core.livedata.nullSafeValue
import com.kevalpatel2106.yip.core.setToDayMax
import com.kevalpatel2106.yip.core.setToDayMin
import com.kevalpatel2106.yip.entity.getDeadlineColor
import com.kevalpatel2106.yip.entity.isPreBuild
import com.kevalpatel2106.yip.notifications.DeadlineNotificationReceiver
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatter
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.repo.validator.Validator
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.Date
import java.util.concurrent.TimeUnit

internal class EditDeadlineViewModel @ViewModelInject internal constructor(
    @ApplicationContext private val application: Context,
    private val deadlineRepo: DeadlineRepo,
    private val alarmRepo: AlarmRepo,
    private val validator: Validator,
    private val df: DateFormatter,
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    private val titleLength by lazy { application.resources.getInteger(R.integer.max_process_title) }
    private val oneDayMills = TimeUnit.DAYS.toMillis(1)
    private var deadlineId: Long = NEW_DEADLINE_ID

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var isSomethingChanged = false

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var isPremiumUser = false

    private val _viewState = MutableLiveData<EditViewState>(EditViewState.initialState(df))
    val viewState: LiveData<EditViewState> = _viewState

    private val _singleViewState = SingleLiveEvent<EditDeadlineSingleViewState>()
    val singleViewState: LiveData<EditDeadlineSingleViewState> = _singleViewState

    init {
        monitorProStatus()
    }

    private fun monitorProStatus() {
        billingRepo.observeIsPurchased()
            .subscribe { isPro ->
                isPremiumUser = isPro
                _viewState.modify { copy(showLockedColorPicker = !isPro) }
            }
            .addTo(compositeDisposable)
    }

    fun setDeadlineId(deadlineId: Long) {
        if (this.deadlineId != deadlineId) {
            compositeDisposable.clear()
            this.deadlineId = deadlineId
            loadDeadline(deadlineId)
        }
    }

    private fun loadDeadline(deadlineIdToLoad: Long) {
        if (deadlineIdToLoad == NEW_DEADLINE_ID) return

        deadlineRepo.observeDeadline(deadlineIdToLoad)
            .firstOrError()
            .subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
            .doOnSubscribe { setViewLoadingState() }
            .subscribe({ deadline ->
                _viewState.modify {
                    copy(
                        isLoading = false,
                        type = deadline.deadlineType,

                        initialTitle = deadline.title,
                        currentTitle = deadline.title,
                        titleErrorMsg = null,

                        allowEditDate = !deadline.deadlineType.isPreBuild(),
                        startTime = deadline.start,
                        startTimeString = df.formatDateOnly(deadline.start),
                        endTime = deadline.end,
                        endTimeString = df.formatDateOnly(deadline.end),

                        allowEditColor = true,
                        selectedColor = deadline.color,

                        allowEditNotifications = true,
                        notificationList = deadline.notificationPercent
                    )
                }
            }, {
                Timber.e(it)
                _singleViewState.value = ShowUserMessage(
                    application.getString(R.string.dashboard_error_loading_deadline),
                    true
                )
            })
            .addTo(compositeDisposable)
    }

    fun onStartDateClicked() {
        _singleViewState.value = OpenStartDatePicker
    }

    fun onEndDateClicked() {
        _singleViewState.value = OpenEndDatePicker
    }

    fun onStartDateSelected(startDate: Date) {
        val newEndDate =
            if (validator.isValidStartDate(startDate, viewState.nullSafeValue().endTime)) {
                viewState.nullSafeValue().endTime
            } else {
                Date(startDate.time + oneDayMills)
            }

        isSomethingChanged = true
        _viewState.modify {
            copy(
                startTime = startDate,
                startTimeString = df.formatDateOnly(startDate),
                endTime = newEndDate,
                endTimeString = df.formatDateOnly(newEndDate)
            )
        }
    }

    fun onEndDateSelected(endDate: Date) {
        if (validator.isValidEndDate(viewState.nullSafeValue().startTime, endDate)) {
            isSomethingChanged = true
            _viewState.modify {
                copy(endTime = endDate, endTimeString = df.formatDateOnly(endDate))
            }
        } else {
            _singleViewState.value = ShowUserMessage(
                application.getString(R.string.error_start_date_after_end_date),
                false
            )
            _viewState.modify { this }
        }
    }

    fun onColorSelected(@ColorInt color: Int) {
        when {
            !isPremiumUser -> {
                _singleViewState.value = OpenPaymentScreen
            }
            validator.isValidDeadlineColor(color) -> {
                isSomethingChanged = true
                _viewState.modify { copy(selectedColor = getDeadlineColor(color)) }
            }
            else -> {
                _singleViewState.value = ShowUserMessage(
                    application.getString(R.string.error_invalid_deadline),
                    false
                )
                _viewState.modify { this }
            }
        }
    }

    fun onTitleChanged(title: String) {
        when {
            title.trim() == viewState.nullSafeValue().initialTitle.trim() -> {
                return
            }
            validator.isValidTitle(title) -> {
                isSomethingChanged = true
                _viewState.modify { copy(currentTitle = title, titleErrorMsg = null) }
            }
            else -> {
                _viewState.modify {
                    copy(
                        titleErrorMsg = application.getString(
                            R.string.error_deadline_title_long,
                            titleLength
                        )
                    )
                }
            }
        }
    }

    fun onAddNotificationClicked() {
        _singleViewState.value = if (isPremiumUser) {
            ShowNotificationPicker
        } else {
            OpenPaymentScreen
        }
    }

    fun onNotificationAdded(notificationPercent: Float) {
        val list = viewState.nullSafeValue().notificationList
            .toMutableList()
            .apply { add(notificationPercent) }
            .distinct()

        isSomethingChanged = true
        _viewState.modify { copy(notificationList = list) }
    }

    fun onNotificationRemoved(notificationPercent: Float) {
        val list = viewState.nullSafeValue().notificationList
            .toMutableList()
            .apply { remove(notificationPercent) }

        isSomethingChanged = true
        _viewState.modify { copy(notificationList = list) }
    }

    fun onClosePressed() {
        if (viewState.nullSafeValue().isLoading) return
        _singleViewState.value = if (isSomethingChanged) {
            ShowConfirmationDialog
        } else {
            CloseScreen
        }
    }

    fun saveDeadline() {
        _viewState.value?.run {
            if (isLoading || !isSomethingChanged || !areAllInputsValid()) return

            deadlineRepo.addUpdateDeadline(
                deadlineId = deadlineId,
                title = currentTitle.capitalize(),
                startTime = startTime.apply { setToDayMin() },
                endTime = endTime.apply { setToDayMax() },
                color = selectedColor,
                type = type,
                notifications = notificationList
            ).ignoreElement()
                .subscribeOn(RxSchedulers.database)
                .observeOn(RxSchedulers.main)
                .doOnSubscribe { setViewLoadingState() }
                .subscribe({
                    alarmRepo.updateAlarms(DeadlineNotificationReceiver::class.java)
                    _singleViewState.value = ShowUserMessage(
                        application.getString(R.string.deadline_saved_success),
                        true
                    )
                }, {
                    Timber.e(it)
                    _viewState.modify {
                        copy(
                            isLoading = false,
                            allowEditColor = true,
                            allowEditNotifications = true,
                            allowEditDate = !type.isPreBuild()
                        )
                    }
                    _singleViewState.value = ShowUserMessage(
                        application.getString(R.string.error_deadline_save),
                        false
                    )
                })
                .addTo(compositeDisposable)
        }
    }

    private fun setViewLoadingState() {
        _viewState.modify {
            copy(
                isLoading = true,
                allowEditColor = false,
                allowEditNotifications = false,
                allowEditDate = false
            )
        }
    }

    private fun EditViewState.areAllInputsValid(): Boolean {
        if (!validator.isValidTitle(currentTitle)) {
            _viewState.modify {
                copy(
                    titleErrorMsg = application.getString(
                        R.string.error_deadline_title_long,
                        titleLength
                    )
                )
            }
            return false
        }

        if (!validator.isValidStartDate(startTime, endTime)) {
            _singleViewState.value = ShowUserMessage(
                application.getString(R.string.error_start_date_after_end_date),
                false
            )
            return false
        }

        if (!validator.isValidEndDate(startTime, endTime)) {
            _singleViewState.value = ShowUserMessage(
                application.getString(R.string.error_start_date_after_end_date),
                false
            )
            return false
        }

        if (!validator.isValidDeadlineColor(selectedColor.colorInt)) {
            _singleViewState.value = ShowUserMessage(
                application.getString(R.string.error_invalid_deadline),
                false
            )
            return false
        }

        if (!validator.isValidNotification(notificationList)) {
            _singleViewState.value = ShowUserMessage(
                application.getString(R.string.invalid_notification_percent),
                false
            )
            return false
        }
        return true
    }

    companion object {
        const val NEW_DEADLINE_ID = 0L
    }
}
