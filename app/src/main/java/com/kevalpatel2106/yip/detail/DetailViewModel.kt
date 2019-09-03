package com.kevalpatel2106.yip.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.SignalLiveEvent
import com.kevalpatel2106.yip.core.SingleLiveEvent
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.emptySpannableString
import com.kevalpatel2106.yip.core.getBackgroundGradient
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.repo.utils.DateFormatter
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import timber.log.Timber
import javax.inject.Inject

internal class DetailViewModel @Inject internal constructor(
    private val application: Application,
    private val yipRepo: YipRepo,
    private val appShortcutHelper: AppShortcutHelper,
    private val sdf: DateFormatter
) : BaseViewModel() {

    private var progressId: Long = -1

    private val _viewState = MutableLiveData<DetailViewState>(
        DetailViewState.initialState(application)
    )
    val viewState: LiveData<DetailViewState> = _viewState

    private val _userMessage = SingleLiveEvent<String>()
    internal val userMessage: LiveData<String> = _userMessage

    private val _closeDetail = SignalLiveEvent()
    internal val closeDetail: LiveData<Unit> = SignalLiveEvent()

    private fun monitorProgress(id: Long) {
        yipRepo.observeProgress(id)
            .subscribe({ item ->
                val isProgressComplete = item.percent >= 100f

                _viewState.value = _viewState.value?.copy(
                    cardBackground = application.getBackgroundGradient(item.color.value),

                    progressTitleText = item.title,
                    progressPercentText = application.getString(
                        R.string.progress_percentage,
                        item.percent
                    ),
                    progressTimeLeftText = if (isProgressComplete) {
                        emptySpannableString()
                    } else {
                        DetailUseCase.prepareTimeLeft(
                            application = application,
                            endTime = item.end,
                            secondaryColor = item.color.value
                        )
                    },
                    progressEndTimeText = sdf.format(item.end),
                    progressStartTimeText = sdf.format(item.start),

                    detailFlipperPosition = if (isProgressComplete) {
                        ProgressFlipper.POS_SHARE_PROGRESS
                    } else {
                        ProgressFlipper.POS_TIME_LEFT
                    },

                    progressPercent = item.percent.toInt()
                )
            }, { throwable ->
                Timber.e(throwable)
                _userMessage.value = throwable.message
                _closeDetail.sendSignal()
            })
            .addTo(compositeDisposable)
    }

    internal fun deleteProgress() {
        yipRepo.deleteProgress(progressId)
            .doOnSubscribe { _viewState.value = _viewState.value?.copy(isDeletingProgress = true) }
            .doOnTerminate { _viewState.value = _viewState.value?.copy(isDeletingProgress = false) }
            .subscribe({
                _userMessage.value = application.getString(R.string.progress_delete_successful)
                _closeDetail.sendSignal()
            }, { throwable ->
                Timber.e(throwable)
                _userMessage.value = throwable.message
            })
            .addTo(compositeDisposable)
    }

    internal fun requestPinShortcut() {
        val title = viewState.value?.progressTitleText
            ?: application.getString(R.string.application_name)
        val launchIntent = AppLaunchHelper.launchWithProgressDetail(application, progressId)
        appShortcutHelper.requestPinShortcut(title, launchIntent)
    }

    internal fun setProgressIdToMonitor(progressId: Long) {
        if (this.progressId != progressId) {
            this.progressId = progressId
            monitorProgress(progressId)
        }
    }
}
