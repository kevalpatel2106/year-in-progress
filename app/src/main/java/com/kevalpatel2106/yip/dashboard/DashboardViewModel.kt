package com.kevalpatel2106.yip.dashboard

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.getBackgroundGradient
import com.kevalpatel2106.yip.core.livedata.SignalLiveEvent
import com.kevalpatel2106.yip.core.livedata.SingleLiveEvent
import com.kevalpatel2106.yip.core.livedata.recall
import com.kevalpatel2106.yip.core.openPlayStorePage
import com.kevalpatel2106.yip.core.updateWidgets
import com.kevalpatel2106.yip.dashboard.adapter.listItem.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.DeadlineListItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.EmptyRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ErrorRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ListItemRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.LoadingRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.PaddingItem
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.repo.utils.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import kotlin.random.Random

internal class DashboardViewModel @ViewModelInject constructor(
    @ApplicationContext private val application: Context,
    private val deadlineRepo: DeadlineRepo,
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val billingRepo: BillingRepo,
    private val appShortcutHelper: AppShortcutHelper
) : BaseViewModel() {
    private val _deadlines = MutableLiveData<ArrayList<ListItemRepresentable>>(arrayListOf())
    internal val deadlines: LiveData<ArrayList<ListItemRepresentable>> = _deadlines

    private val _askForRatingSignal = SignalLiveEvent()
    internal val askForRatingSignal: LiveData<Unit> = _askForRatingSignal

    private val _showInterstitialAdSignal = SignalLiveEvent()
    internal val showInterstitialAdSignal: LiveData<Unit> = _showInterstitialAdSignal

    private val _userMessages = SingleLiveEvent<String>()
    internal val userMessages: LiveData<String> = _userMessages

    private var _expandDeadline = MutableLiveData<Long>(RESET_COLLAPSED_ID)
    internal var expandDeadline: LiveData<Long> = _expandDeadline

    init {
        monitorDeadlines()
    }

    private fun monitorDeadlines() {
        Flowable.combineLatest(
            deadlineRepo.observeAllDeadlines(),
            billingRepo.observeIsPurchased().toFlowable(BackpressureStrategy.BUFFER),
            BiFunction<List<Deadline>, Boolean, Pair<List<Deadline>, Boolean>> { list, isPro -> list to isPro }
        ).doOnSubscribe {
            // Show the loader.
            _deadlines.value?.apply {
                clear()
                add(LoadingRepresentable)
            }
            _deadlines.recall()
        }.doOnNext { (deadlines, _) ->
            if (!deadlines.any { it.id == _expandDeadline.value }) {
                // Close any deleted deadline detail
                resetExpandedDeadline()
            }
            appShortcutHelper.updateDynamicShortcuts(deadlines)
            application.updateWidgets()
        }.map { (deadlines, isPro) ->

            // Add Ads if the user is not pro.
            @Suppress("UNCHECKED_CAST")
            val list = deadlines.map {
                DeadlineListItem(
                    deadline = it,
                    deadlineString = application.getString(
                        R.string.deadline_percentage,
                        it.percent
                    ),
                    backgroundGradient = application.getBackgroundGradient(it.color.colorInt)
                )
            }.toMutableList() as ArrayList<ListItemRepresentable>

            return@map list.apply {
                // Add the ads if the user is not pro.
                if (isNotEmpty() && !isPro) {
                    add(
                        if (size > MAX_POSITION_OF_AD) MAX_POSITION_OF_AD else size,
                        AdsItem
                    )
                }
            }
        }.subscribe({ listItems ->
            _deadlines.value?.apply {
                clear()
                if (listItems.isEmpty()) {
                    // Show the empty list view.
                    add(EmptyRepresentable(application.getString(R.string.dashboard_no_deadline_message)))
                } else {
                    // Show all the deadline.
                    listItems.add(PaddingItem)
                    addAll(listItems)
                }
            }
            _deadlines.recall()
        }, { throwable ->
            Timber.e(throwable)

            // Display error.
            _deadlines.value?.apply {
                clear()
                add(
                    ErrorRepresentable(
                        application.getString(R.string.dashboard_error_loading_deadline)
                    ) { monitorDeadlines() }
                )
            }
            _deadlines.recall()
        }).addTo(compositeDisposable)
    }

    internal fun userWantsToRateNow() {
        application.openPlayStorePage()
        sharedPrefsProvider.savePreferences(PREF_KEY_NEVER_ASK_RATE, true)
    }

    internal fun userWantsToNeverRate() {
        sharedPrefsProvider.savePreferences(PREF_KEY_NEVER_ASK_RATE, true)
    }

    internal fun userWantsToOpenDetail(
        deadlineId: Long,
        @VisibleForTesting randomNum: Int = Random.nextInt(MAX_RANDOM_NUMBER)
    ) {
        deadlineRepo.isDeadlineExist(deadlineId)
            .subscribe({ exist ->
                if (exist) {
                    _expandDeadline.value = deadlineId
                    handleRandomEvents(randomNum)
                } else {
                    _userMessages.value = application.getString(R.string.error_deadline_not_exist)
                }
            }, { throwable ->
                Timber.e(throwable)
                _userMessages.value = application.getString(R.string.error_deadline_not_exist)
            })
            .addTo(compositeDisposable)
    }

    private fun handleRandomEvents(randomNum: Int) {
        // Should show rating dialog?
        if (!sharedPrefsProvider.getBoolFromPreference(
                PREF_KEY_NEVER_ASK_RATE,
                false
            ) && randomNum == RANDOM_NUMBER_FOR_RATING
        ) {
            _askForRatingSignal.sendSignal()
        }

        // Should show ads?
        if (randomNum == RANDOM_NUMBER_FOR_AD && !billingRepo.isPurchased()) {
            _showInterstitialAdSignal.sendSignal()
        }
    }

    internal fun isDetailExpanded(): Boolean = expandDeadline.value != RESET_COLLAPSED_ID

    internal fun resetExpandedDeadline() {
        _expandDeadline.value = RESET_COLLAPSED_ID
    }

    companion object {
        internal const val RESET_COLLAPSED_ID = -1L
        private const val MAX_POSITION_OF_AD = 4
        private const val MAX_RANDOM_NUMBER = 14

        @VisibleForTesting
        internal const val RANDOM_NUMBER_FOR_RATING = 1

        @VisibleForTesting
        internal const val RANDOM_NUMBER_FOR_AD = 0

        @VisibleForTesting
        internal const val PREF_KEY_NEVER_ASK_RATE = "pref_key_rated"
    }
}
