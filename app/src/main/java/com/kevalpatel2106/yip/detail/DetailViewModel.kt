package com.kevalpatel2106.yip.detail

import android.app.Application
import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.SingleLiveEvent
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.repo.utils.DateFormatter
import com.kevalpatel2106.yip.repo.utils.NtpProvider
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


internal class DetailViewModel @Inject internal constructor(
        private val application: Application,
        private val yipRepo: YipRepo,
        private val ntpProvider: NtpProvider,
        private val sdf: DateFormatter
) : BaseViewModel() {

    internal var progressId: Long = 0
        set(value) {
            field = value
            monitorProgress(value)
        }

    internal val progressTitle = MutableLiveData<String>()
    internal val progressPercent = MutableLiveData<Float>()
    internal val progressStartTime = MutableLiveData<String>()
    internal val progressEndTime = MutableLiveData<String>()
    internal val progressColor = MutableLiveData<Int>()
    internal val progressTimeLeft = MutableLiveData<SpannableString>()

    internal val isProgressComplete = MutableLiveData<Boolean>()
    internal val isDeleting = MutableLiveData<Boolean>()
    internal val isLoading = MutableLiveData<Boolean>()
    internal val errorMessage = SingleLiveEvent<String>()
    internal val closeDetail = SingleLiveEvent<Unit>()

    private fun monitorProgress(id: Long) {
        yipRepo.observeProgress(id)
                .doOnSubscribe {
                    isLoading.value = true
                }.doAfterTerminate {
                    isLoading.value = false
                }.subscribe({ item ->
                    progressTitle.value = item.title
                    progressStartTime.value = sdf.format(item.start)
                    progressEndTime.value = sdf.format(item.end)
                    progressPercent.value = item.percent(ntpProvider.now())
                    progressColor.value = item.color.value

                    if (item.end.before(Date(System.currentTimeMillis()))) {
                        isProgressComplete.value = true
                    } else {
                        progressTimeLeft.value = prepareTimeLeft(item.end, item.color)
                        isProgressComplete.value = false
                    }
                }, {
                    errorMessage.value = it.message
                    closeDetail.value = Unit
                })
                .addTo(compositeDisposable)
    }

    internal fun deleteProgress() {
        yipRepo.deleteProgress(progressId)
                .subscribe({
                    errorMessage.value = application.getString(R.string.progress_delete_successful)
                    closeDetail.value = Unit
                }, {
                    errorMessage.value = it.message
                    closeDetail.value = Unit
                })
                .addTo(compositeDisposable)
    }

    private fun prepareTimeLeft(endTime: Date, progressColor: ProgressColor): SpannableString {
        // Find difference in mills
        var diffMills = endTime.time - System.currentTimeMillis()
        if (diffMills < 0) return SpannableString("")

        // Calculate the days, hours and minutes
        val days = TimeUnit.DAYS.convert(diffMills, TimeUnit.MILLISECONDS)
        if (days != 0L) diffMills %= TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS)
        val hours = TimeUnit.HOURS.convert(diffMills, TimeUnit.MILLISECONDS)
        if (hours != 0L) diffMills %= TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS)
        val mins = TimeUnit.MINUTES.convert(diffMills, TimeUnit.MILLISECONDS)

        // Prepare raw string
        val rawString = application.getString(
                R.string.time_left_title,
                days,
                application.resources.getQuantityString(R.plurals.days, days.toInt()),
                hours,
                application.resources.getQuantityString(R.plurals.hours, hours.toInt()),
                mins,
                application.resources.getQuantityString(R.plurals.minutes, mins.toInt())
        )

        return SpannableString(rawString).apply {
            setSpan(
                    RelativeSizeSpan(0.8f),
                    0,
                    rawString.indexOfFirst { it == '\n' },
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Set spans for days left
            val dayStartIndex = rawString.indexOf(days.toString())
            val dayEndIndex = rawString.indexOf(days.toString()) + days.toString().length
            setSpan(
                    ForegroundColorSpan(progressColor.value),
                    dayStartIndex,
                    dayEndIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                    RelativeSizeSpan(1.3f),
                    dayStartIndex,
                    dayEndIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Set spans for hours left
            val hoursStartIndex = rawString.indexOf(hours.toString(), dayEndIndex)
            val hoursEndIndex = hoursStartIndex + hours.toString().length
            setSpan(
                    ForegroundColorSpan(progressColor.value),
                    hoursStartIndex,
                    hoursEndIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                    RelativeSizeSpan(1.3f),
                    hoursStartIndex,
                    hoursEndIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Set spans for minutes left
            val minsStartIndex = rawString.indexOf(mins.toString(), hoursEndIndex)
            val minsEndIndex = minsStartIndex + mins.toString().length
            setSpan(
                    ForegroundColorSpan(progressColor.value),
                    minsStartIndex,
                    minsEndIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                    RelativeSizeSpan(1.3f),
                    minsStartIndex,
                    minsEndIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    internal fun prepareShareAchievement(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            progressTitle.value?.let { title ->
                putExtra(Intent.EXTRA_TEXT, application.getString(R.string.achivement_share_message, title))
            }
            type = "text/plain"
        }
    }
}