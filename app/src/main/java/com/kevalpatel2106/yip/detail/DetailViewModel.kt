package com.kevalpatel2106.yip.detail

import android.app.Application
import android.text.SpannableString
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.SignalLiveData
import com.kevalpatel2106.yip.core.SingleLiveEvent
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.darkenColor
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.repo.utils.DateFormatter
import timber.log.Timber
import javax.inject.Inject


internal class DetailViewModel @Inject internal constructor(
    private val application: Application,
    private val yipRepo: YipRepo,
    private val sdf: DateFormatter
) : BaseViewModel() {

    internal var progressId: Long = 0
        set(value) {
            field = value
            monitorProgress(value)
        }

    val viewState = MutableLiveData<DetailViewState>()
    val isDeleting = MutableLiveData<Boolean>()
    internal val errorMessage = SingleLiveEvent<String>()
    internal val closeDetail = SignalLiveData()

    private fun monitorProgress(id: Long) {
        yipRepo.observeProgress(id)
            .subscribe({ item ->
                val isProgressComplete = item.percent >= 100f

                viewState.value = DetailViewState(
                    backgroundColor = darkenColor(item.color.value),

                    progressTitleText = item.title,

                    progressPercentTextColor = item.color.value,
                    progressPercentText = application.getString(
                        R.string.progress_percentage,
                        item.percent
                    ),

                    progressTimeLeftText = if (isProgressComplete) {
                        SpannableString("")
                    } else {
                        DetailUseCase.prepareTimeLeft(application, item.end, item.color)
                    },
                    progressEndTimeText = sdf.format(item.end),
                    progressStartTimeText = sdf.format(item.start),

                    isProgressComplete = if (isProgressComplete) {
                        ProgressFlipper.POS_SHARE_PROGRESS
                    } else {
                        ProgressFlipper.POS_TIME_LEFT
                    },

                    progressPercent = item.percent.toInt(),
                    progressBarColor = item.color.value,

                    progressAchievementTextColor = item.color.value
                )
            }, {
                Timber.e(it)
                errorMessage.value = it.message
                closeDetail.invoke()
            })
            .addTo(compositeDisposable)
    }

    internal fun deleteProgress() {
        yipRepo.deleteProgress(progressId)
            .subscribe({
                errorMessage.value = application.getString(R.string.progress_delete_successful)
                closeDetail.invoke()
            }, {
                Timber.e(it)
                errorMessage.value = it.message
                closeDetail.invoke()
            })
            .addTo(compositeDisposable)
    }
}