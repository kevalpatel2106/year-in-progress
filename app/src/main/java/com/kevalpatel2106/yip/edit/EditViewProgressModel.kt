package com.kevalpatel2106.yip.edit

import android.app.Application
import androidx.annotation.ColorInt
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.*
import com.kevalpatel2106.yip.entity.*
import com.kevalpatel2106.yip.notifications.ProgressNotificationReceiver
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import com.kevalpatel2106.yip.repo.providers.AlarmProvider
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class EditViewProgressModel @Inject internal constructor(
        private val application: Application,
        private val yipRepo: YipRepo,
        private val alarmProvider: AlarmProvider
) : BaseViewModel() {
    private val titleLength by lazy { application.resources.getInteger(R.integer.max_process_title) }
    private var progressTypeType: ProgressType = ProgressType.CUSTOM
    internal var progressId: Long = 0
        set(value) {
            if (value > 0L) monitorProgress(value)
            field = value
        }

    // Progress fields
    private var currentTitle: String? = null

    internal val initialTitle = MutableLiveData<String>()
    internal val currentStartDate = MutableLiveData<Date>()
    internal val currentEndDate = MutableLiveData<Date>()
    internal val currentColor = MutableLiveData<ProgressColor>()
    internal val lockColorPicker = MutableLiveData<Boolean>()
    internal val isPrebuiltProgress = MutableLiveData<Boolean>()
    internal val currentNotificationsList = MutableLiveData<List<Float>>()

    internal val colors = MutableLiveData<IntArray>()
    internal var isSomethingChanged: Boolean = false
    internal var isTitleChanged: Boolean = false
    internal val closeSignal = SingleLiveEvent<Boolean>()

    // Errors
    internal val errorInvalidTitle = SingleLiveEvent<String>()
    internal val userMessage = SingleLiveEvent<String>()

    // Loaders
    internal val isLoadingProgress = MutableLiveData<Boolean>()

    init {
        colors.value = ProgressColor.values().map { it.value }.toIntArray()
        isLoadingProgress.value = false
        isPrebuiltProgress.value = false
        lockColorPicker.value = false

        // Initial progress values.
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
        initialTitle.value = ""
        currentStartDate.value = Date(System.currentTimeMillis()).apply { setToDayMin() }
        currentEndDate.value = Date(tomorrow.timeInMillis).apply { setToDayMax() }
        currentColor.value = ProgressColor.COLOR_BLUE
        currentNotificationsList.value = listOf()

        // Monitor the pro status
        BillingRepo.isPurchased.subscribe { lockColorPicker.value = !it }.addTo(compositeDisposable)
    }

    private fun monitorProgress(id: Long) {
        yipRepo.observeProgress(id)
                .firstOrError()
                .doOnSubscribe {
                    isLoadingProgress.value = true
                }.doAfterTerminate {
                    isLoadingProgress.value = false
                }.subscribe({ progress ->
                    progressTypeType = progress.progressType
                    isPrebuiltProgress.value = progress.progressType.isPreBuild()

                    currentTitle = progress.title
                    initialTitle.value = progress.title
                    currentColor.value = progress.color
                    currentEndDate.value = progress.end
                    currentStartDate.value = progress.start
                    currentNotificationsList.value = progress.notificationPercent
                }, {
                    Timber.e(it)
                    userMessage.value = it.message
                    closeSignal.value = true
                })
                .addTo(compositeDisposable)
    }

    internal fun onProgressStartDateSelected(startDate: Date) {
        isSomethingChanged = true
        currentStartDate.value = startDate
        if (currentEndDate.value?.before(startDate) != false) {
            currentEndDate.value = Date(startDate.time + TimeUnit.DAYS.toMillis(1))
        }
    }

    internal fun onProgressEndDateSelected(endDate: Date) {
        isSomethingChanged = true
        currentEndDate.value = endDate
    }

    internal fun onProgressColorSelected(@ColorInt color: Int) {
        isSomethingChanged = true
        if (!isValidProgressColor(color)) {
            userMessage.value = application.getString(R.string.error_invalid_progress)
            currentColor.recall()
        }
        if (currentColor.value?.value != color) currentColor.value = getProgressColor(color)
    }

    internal fun onProgressTitleChanged(title: String) {
        currentTitle = title
        isTitleChanged = title != initialTitle.value
        if (title.length !in 0..titleLength) {
            errorInvalidTitle.value = application.getString(R.string.error_progress_title_long, titleLength)
        } else if (errorInvalidTitle.value != "") {
            errorInvalidTitle.value = ""
        }
    }

    internal fun saveProgress(notificationsList: List<Float>) {
        if (isLoadingProgress.value == true) return
        if (currentTitle == null || currentTitle!!.length !in 1..titleLength) {
            errorInvalidTitle.value = application.getString(R.string.error_progress_title_long, titleLength)
            return
        }
        if (currentStartDate.value?.after(currentEndDate.value) == true) {
            userMessage.value = application.getString(R.string.error_start_date_after_end_date)
            return
        }
        if (!isValidProgressColor(currentColor.value!!.value)) {
            userMessage.value = application.getString(R.string.error_invalid_progress)
            return
        }

        // Sanitize data
        val title =
                currentTitle?.capitalize()
                    ?: throw IllegalStateException("Progress title cannot be null.")
        val startDate =
                currentStartDate.value?.apply { setToDayMin() }
                    ?: throw IllegalStateException("Start date cannot be null.")
        val endDate =
                currentEndDate.value?.apply { setToDayMax() }
                    ?: throw IllegalStateException("End date cannot be null.")

        yipRepo.addUpdateProgress(
                processId = progressId,
                title = title,
                startTime = startDate,
                endTime = endDate,
                progressTypeType = progressTypeType,
                color = currentColor.value ?: throw IllegalStateException("Color cannot be null."),
                notifications = notificationsList
        ).doOnSubscribe {
            isLoadingProgress.value = true
        }.doOnSuccess {
            userMessage.value = application.getString(R.string.progress_saved_success)
        }.delay(2500, TimeUnit.MILLISECONDS, RxSchedulers.main)
                .doAfterTerminate {
                    isLoadingProgress.value = false
                }
                .subscribe({
                    alarmProvider.updateAlarms(ProgressNotificationReceiver::class.java)
                    closeSignal.value = true
                }, {
                    Timber.e(it)
                    userMessage.value = it.message
                })
                .addTo(compositeDisposable)
    }
}