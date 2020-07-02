package com.kevalpatel2106.yip.detail

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.darkenColor
import com.kevalpatel2106.yip.core.emptySpannableString
import com.kevalpatel2106.yip.core.getBackgroundGradient
import com.kevalpatel2106.yip.core.livedata.SignalLiveEvent
import com.kevalpatel2106.yip.core.livedata.SingleLiveEvent
import com.kevalpatel2106.yip.entity.isRepeatable
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.repo.utils.dateFormatter.DateFormatter
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber

internal class DetailViewModel @ViewModelInject internal constructor(
    @ApplicationContext private val application: Context,
    private val deadlineRepo: DeadlineRepo,
    private val appShortcutHelper: AppShortcutHelper,
    private val sdf: DateFormatter
) : BaseViewModel() {

    private var deadlineId = -1L

    private val _viewState = MutableLiveData<DetailViewState>(
        DetailViewState.initialState(application)
    )
    val viewState: LiveData<DetailViewState> = _viewState

    private val _userMessage = SingleLiveEvent<String>()
    internal val userMessage: LiveData<String> = _userMessage

    private val _closeDetail = SignalLiveEvent()
    internal val closeDetail: LiveData<Unit> =
        SignalLiveEvent()

    private fun monitorDeadlines(id: Long) {
        deadlineRepo.observeDeadline(id)
            .subscribe({ item ->
                val isDeadlineComplete = item.percent >= 100f
                val deadlineThemeColor = darkenColor(item.color.colorInt, 0.9f)

                _viewState.value = _viewState.value?.copy(
                    cardBackground = application.getBackgroundGradient(item.color.colorInt),

                    titleText = item.title,
                    deadlinePercentText = application.getString(
                        R.string.deadline_percentage,
                        item.percent
                    ),
                    timeLeftText = if (isDeadlineComplete) {
                        emptySpannableString()
                    } else {
                        DetailUseCase.prepareTimeLeft(
                            application = application,
                            endTime = item.end,
                            secondaryColor = deadlineThemeColor
                        )
                    },
                    endTimeText = sdf.format(item.end),
                    startTimeText = sdf.format(item.start),
                    deadlineColor = deadlineThemeColor,
                    showRepeatable = item.deadlineType.isRepeatable(),

                    detailFlipperPosition = if (isDeadlineComplete) {
                        DetailViewFlipper.POS_SHARE
                    } else {
                        DetailViewFlipper.POS_TIME_LEFT
                    },

                    percent = item.percent.toInt()
                )
            }, { throwable ->
                Timber.e(throwable)
                _userMessage.value = throwable.message
                _closeDetail.sendSignal()
            })
            .addTo(compositeDisposable)
    }

    internal fun deleteDeadline() {
        deadlineRepo.deleteDeadline(deadlineId)
            .doOnSubscribe { _viewState.value = _viewState.value?.copy(isDeleting = true) }
            .doOnTerminate { _viewState.value = _viewState.value?.copy(isDeleting = false) }
            .subscribe({
                _userMessage.value = application.getString(R.string.deadline_delete_successful)
                _closeDetail.sendSignal()
            }, { throwable ->
                Timber.e(throwable)
                _userMessage.value = throwable.message
            })
            .addTo(compositeDisposable)
    }

    internal fun requestPinShortcut() {
        val title = viewState.value?.titleText
            ?: application.getString(R.string.application_name)
        val launchIntent = AppLaunchHelper.launchWithDeadlineDetail(application, deadlineId)
        appShortcutHelper.requestPinShortcut(title, launchIntent)
    }

    internal fun setDeadlineIdToMonitor(deadlineId: Long) {
        if (this.deadlineId != deadlineId) {
            this.deadlineId = deadlineId
            monitorDeadlines(deadlineId)
        }
    }
}
