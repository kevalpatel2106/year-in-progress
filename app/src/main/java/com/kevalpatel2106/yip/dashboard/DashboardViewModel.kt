package com.kevalpatel2106.yip.dashboard

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.RxSchedulers
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.getBackgroundGradient
import com.kevalpatel2106.yip.core.livedata.SingleLiveEvent
import com.kevalpatel2106.yip.core.livedata.modify
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
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
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
    private val _deadlines = MutableLiveData<List<ListItemRepresentable>>(arrayListOf())
    internal val deadlines: LiveData<List<ListItemRepresentable>> = _deadlines

    private val _singleEvents = SingleLiveEvent<DashboardSingleEvent>()
    internal val singleEvents: LiveData<DashboardSingleEvent> = _singleEvents

    private var _expandViewState = MutableLiveData<DashboardExpandedViewState>(DetailViewCollapsed)
    internal var expandViewState: LiveData<DashboardExpandedViewState> = _expandViewState

    init {
        monitorDeadlines()
    }

    private fun monitorDeadlines() {
        Flowable.combineLatest(
            deadlineRepo.observeAllDeadlines(),
            billingRepo.observeIsPurchased().toFlowable(BackpressureStrategy.BUFFER),
            BiFunction<List<Deadline>, Boolean, Pair<List<Deadline>, Boolean>> { list, isPro -> list to isPro }
        ).subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
            .doOnSubscribe {
                _deadlines.modify { mutableListOf(LoadingRepresentable) }
            }.doOnNext { (deadlines, _) ->
                if (isDetailExpanded() && !deadlines.any { it.id == expandedDeadlineId() }) {
                    onCloseDeadlineDetail()
                }
            }.doOnNext { (deadlines, _) ->
                appShortcutHelper.updateDynamicShortcuts(deadlines)
                application.updateWidgets()
            }.map { (deadlines, isPro) ->
                val list: MutableList<ListItemRepresentable> = deadlines.map {
                    DeadlineListItem(
                        deadline = it,
                        percentString = application.getString(
                            R.string.deadline_percentage,
                            it.percent
                        ),
                        backgroundGradient = application.getBackgroundGradient(it.color.colorInt)
                    )
                }.toMutableList()

                // Add the ads if the user is not pro.
                if (list.isNotEmpty() && !isPro) {
                    val adPosition = if (list.size > AD_POSITION) AD_POSITION else list.size
                    list.add(adPosition, AdsItem)
                }

                list
            }.subscribe({ newList ->
                _deadlines.modify {
                    if (newList.isEmpty()) {
                        mutableListOf(EmptyRepresentable(application.getString(R.string.dashboard_no_deadline_message)))
                    } else {
                        newList.apply { add(PaddingItem) }
                    }
                }
            }, ::handleErrorMonitoringDeadline)
            .addTo(compositeDisposable)
    }

    private fun handleErrorMonitoringDeadline(throwable: Throwable) {
        Timber.e(throwable)
        _deadlines.modify {
            mutableListOf(
                ErrorRepresentable(
                    application.getString(R.string.dashboard_error_loading_deadline)
                ) { monitorDeadlines() }
            )
        }
    }

    internal fun onRateNowClicked() {
        _singleEvents.value = OpenPlayStore
        sharedPrefsProvider.savePreferences(PREF_KEY_NEVER_ASK_RATE, true)
    }

    internal fun onRateNeverClicked() {
        sharedPrefsProvider.savePreferences(PREF_KEY_NEVER_ASK_RATE, true)
    }

    internal fun onOpenDeadlineDetail(
        deadlineId: Long,
        randomNum: Int = Random.nextInt(MAX_RANDOM_NUMBER)
    ) {
        deadlineRepo.isDeadlineExist(deadlineId)
            .subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
            .subscribe({ exist ->
                if (exist) {
                    _expandViewState.value = DetailViewExpanded(deadlineId)
                    handleRandomEvents(randomNum)
                } else {
                    _singleEvents.value = ShowUserMessage(
                        application.getString(R.string.error_deadline_not_exist)
                    )
                }
            }, { throwable ->
                Timber.e(throwable)
                _singleEvents.value = ShowUserMessage(
                    application.getString(R.string.error_deadline_not_exist)
                )
            })
            .addTo(compositeDisposable)
    }

    fun onAddNewButtonClicked() {
        val expandedId = expandedDeadlineId()
        if (expandedId != null) {
            _singleEvents.value = OpenEditDeadline(expandedId)
        } else {
            _singleEvents.value = OpenCreateNewDeadline
        }
    }

    internal fun onCloseDeadlineDetail() {
        _expandViewState.value = DetailViewCollapsed
    }

    internal fun onBackPressed() {
        if (isDetailExpanded()) {
            onCloseDeadlineDetail()
        } else {
            _singleEvents.value = CloseScreen
        }
    }

    internal fun onHamburgerMenuClicked() {
        if (!isDetailExpanded()) _singleEvents.value = OpenBottomNavigationSheet
    }

    private fun handleRandomEvents(randomNum: Int) {
        // Should show rating dialog?
        if (!sharedPrefsProvider.getBoolFromPreference(
                PREF_KEY_NEVER_ASK_RATE,
                false
            ) && randomNum == RANDOM_NUMBER_FOR_RATING
        ) {
            _singleEvents.value = AskForRating
        }

        // Should show ads?
        if (randomNum == RANDOM_NUMBER_FOR_AD && !billingRepo.isPurchased()) {
            _singleEvents.value = ShowInterstitialAd
        }
    }

    private fun isDetailExpanded(): Boolean = expandViewState.value is DetailViewExpanded

    /**
     * Returns the id of expanded [Deadline] if any expanded.
     *
     * @return [Long] id of expanded [Deadline] if any deadline detail is expanded or null if no
     * deadline is expanded
     */
    private fun expandedDeadlineId() = (expandViewState.value as? DetailViewExpanded)?.deadlineId

    companion object {
        private const val AD_POSITION = 4
        private const val MAX_RANDOM_NUMBER = 14

        @VisibleForTesting
        internal const val RANDOM_NUMBER_FOR_RATING = 1

        @VisibleForTesting
        internal const val RANDOM_NUMBER_FOR_AD = 0

        @VisibleForTesting
        internal const val PREF_KEY_NEVER_ASK_RATE = "pref_key_rated"
    }
}
