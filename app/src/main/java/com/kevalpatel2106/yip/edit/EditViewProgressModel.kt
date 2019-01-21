package com.kevalpatel2106.yip.edit

import android.app.Application
import androidx.annotation.ColorInt
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.*
import com.kevalpatel2106.yip.entity.*
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class EditViewProgressModel @Inject internal constructor(
    private val application: Application,
    private val yipRepo: YipRepo
) : BaseViewModel() {
    private val titleLength by lazy { application.resources.getInteger(R.integer.max_process_title) }
    private var progressType: PrebuiltProgress = PrebuiltProgress.CUSTOM
    internal var progressId: Long = 0
        set(value) {
            if (value > 0L) monitorProgress(value)
            field = value
        }

    // Progress fields
    internal val currentTitle = MutableLiveData<String>()
    internal val currentStartDate = MutableLiveData<Date>()
    internal val currentEndDate = MutableLiveData<Date>()
    internal val currentColor = MutableLiveData<ProgressColor>()

    // Errors
    internal val errorInvalidTitle = SingleLiveEvent<String>()
    internal val userMessage = SingleLiveEvent<String>()
    internal val close = SingleLiveEvent<Boolean>()

    // Loaders
    internal val isLoadingProgress = MutableLiveData<Boolean>()
    internal val isPrebuiltProgress = MutableLiveData<Boolean>()

    internal val colors = MutableLiveData<IntArray>()
    internal var isSomethingChanged: Boolean = false

    init {
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }

        colors.value = ProgressColor.values().map { it.value }.toIntArray()
        isLoadingProgress.value = false

        // Initial progress values.
        currentTitle.value = ""
        currentStartDate.value = Date(System.currentTimeMillis())
        currentEndDate.value = Date(tomorrow.timeInMillis)
        currentColor.value = ProgressColor.COLOR_BLUE
    }

    private fun monitorProgress(id: Long) {
        yipRepo.observeProgress(id)
            .firstOrError()
            .doOnSubscribe {
                isLoadingProgress.value = true
            }.doAfterTerminate {
                isLoadingProgress.value = false
            }.subscribe({ progress ->
                progressType = progress.prebuiltProgress
                isPrebuiltProgress.value = progress.prebuiltProgress.isPreBuild()
                currentTitle.value = progress.title
                currentColor.value = progress.color
                currentEndDate.value = progress.end
                currentStartDate.value = progress.start
            }, {
                it.printStackTrace()
                userMessage.value = it.message
                close.value = true
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
        currentTitle.value = title
        isSomethingChanged = true
        if (title.length !in 0..titleLength) {
            errorInvalidTitle.value = application.getString(R.string.error_progress_title_long, titleLength)
        } else {
            errorInvalidTitle.value = ""
        }
    }

    internal fun saveProgress() {
        if (isLoadingProgress.value == true) return
        if (currentTitle.value == null || currentTitle.value!!.length !in 1..titleLength) {
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
            currentTitle.value?.capitalize() ?: throw IllegalStateException("Progress title cannot be null.")
        val startDate =
            currentStartDate.value?.apply { setToDayMin() } ?: throw IllegalStateException("Start date cannot be null.")
        val endDate =
            currentEndDate.value?.apply { setToDayMax() } ?: throw IllegalStateException("End date cannot be null.")

        yipRepo.addUpdateProgress(
            processId = progressId,
            title = title,
            startTime = startDate,
            endTime = endDate,
            progressType = progressType,
            color = currentColor.value ?: throw IllegalStateException("Color cannot be null.")
        ).doOnSubscribe {
            isLoadingProgress.value = true
        }.doOnSuccess {
            userMessage.value = application.getString(R.string.progress_saved_success)
        }.delay(2500, TimeUnit.MILLISECONDS, RxSchedulers.main)
            .doAfterTerminate {
                isLoadingProgress.value = false
            }.subscribe({
                close.value = true
            }, {
                userMessage.value = it.message
            }).addTo(compositeDisposable)
    }
}