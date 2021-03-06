package com.kevalpatel2106.yip.detail

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.AppConstants
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.RxSchedulers
import com.kevalpatel2106.yip.core.ext.addTo
import com.kevalpatel2106.yip.core.ext.darkenColor
import com.kevalpatel2106.yip.core.ext.getBackgroundGradient
import com.kevalpatel2106.yip.core.livedata.SingleLiveEvent
import com.kevalpatel2106.yip.core.livedata.modify
import com.kevalpatel2106.yip.core.livedata.nullSafeValue
import com.kevalpatel2106.yip.entity.ext.isRepeatable
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatter
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.utils.AppLaunchIntentProvider
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber

internal class DetailViewModel @ViewModelInject internal constructor(
    @ApplicationContext private val application: Context,
    private val deadlineRepo: DeadlineRepo,
    private val appShortcutHelper: AppShortcutHelper,
    private val appLaunchIntentProvider: AppLaunchIntentProvider,
    private val sdf: DateFormatter
) : BaseViewModel() {
    private var deadlineId = AppConstants.INVALID_DEADLINE_ID

    private val _viewState = MutableLiveData<DetailViewState>(
        DetailViewState.initialState(application)
    )
    val viewState: LiveData<DetailViewState> = _viewState

    private val _singleEvent = SingleLiveEvent<DetailSingleEvent>()
    internal val singleEvent: LiveData<DetailSingleEvent> = _singleEvent

    internal fun setDeadlineIdToMonitor(deadlineId: Long) {
        if (this.deadlineId != deadlineId) {
            this.deadlineId = deadlineId
            monitorDeadlines(deadlineId)
        }
    }

    private fun monitorDeadlines(id: Long) {
        deadlineRepo.observeDeadline(id)
            .subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
            .subscribe({ deadline ->
                val deadlineThemeColor = darkenColor(deadline.color.colorInt, 0.9f)
                val timeLeftString =
                    DetailUseCase.prepareTimeLeft(application, deadline, deadlineThemeColor)

                _viewState.modify {
                    copy(
                        titleText = deadline.title,

                        descriptionText = deadline.description,
                        hasDescription = !deadline.description.isNullOrEmpty(),

                        showRepeatable = deadline.deadlineType.isRepeatable(),
                        timeLeftText = timeLeftString,

                        startTimeText = sdf.format(deadline.start),
                        endTimeText = sdf.format(deadline.end),

                        deadlineColor = deadlineThemeColor,
                        cardBackground = application.getBackgroundGradient(deadline.color.colorInt),

                        percentText = application.getString(
                            R.string.deadline_percentage,
                            deadline.percent
                        ),
                        percent = deadline.percent.toInt()
                    )
                }
            }, { throwable ->
                Timber.e(throwable)
                _singleEvent.value = ShowUserMessage(
                    message = application.getString(R.string.deadline_open_error),
                    closeScreen = true
                )
            })
            .addTo(compositeDisposable)
    }

    internal fun onDeleteDeadlineClicked() {
        _singleEvent.value = ShowDeleteConfirmationDialog(viewState.nullSafeValue().titleText)
    }

    internal fun requestPinShortcut() {
        val title = _viewState.nullSafeValue().titleText
        val launchIntent =
            appLaunchIntentProvider.launchAppWithDeadlineDetailIntent(application, deadlineId)
        appShortcutHelper.requestPinShortcut(deadlineId, title, launchIntent)
    }

    internal fun onDeleteDeadlineConfirmed() {
        deadlineRepo.deleteDeadline(deadlineId)
            .subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
            .doOnSubscribe { _viewState.modify { copy(isDeleting = true) } }
            .doOnTerminate { _viewState.modify { copy(isDeleting = false) } }
            .subscribe({
                _singleEvent.value = ShowUserMessage(
                    message = application.getString(R.string.deadline_delete_successful),
                    closeScreen = true
                )
            }, { throwable ->
                Timber.e(throwable)
                _singleEvent.value = ShowUserMessage(
                    message = application.getString(R.string.deadline_delete_error),
                    closeScreen = false
                )
            })
            .addTo(compositeDisposable)
    }

    fun showDetailOptionsMenu() {
        if (!_viewState.nullSafeValue().isDeleting) _singleEvent.value = OpenPopUpMenu
    }

    fun onCloseButtonClicked() {
        if (!_viewState.nullSafeValue().isDeleting) _singleEvent.value = CloseDetailScreen
    }
}
