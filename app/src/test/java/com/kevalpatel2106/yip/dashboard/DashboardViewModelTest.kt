package com.kevalpatel2106.yip.dashboard

import android.app.Application
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kevalpatel2106.testutils.RxSchedulersOverrideRule
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.dashboard.adapter.listItem.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.EmptyRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ErrorRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ListItemRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.PaddingItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ProgressListItem
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.progressesRepo.ProgressRepo
import com.kevalpatel2106.yip.repo.utils.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyFloat
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class DashboardViewModelTest {
    private val progressMonitorErrorMessage = "test error"
    private val progressNotFoundMessage = "test error 1"
    private val noItemsMessage = "noItems"
    private val dayProgress = Progress(
        id = 38465L,
        title = "Test title",
        color = ProgressColor.COLOR_YELLOW,
        end = Date(System.currentTimeMillis()),
        start = Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)),
        progressType = ProgressType.DAY_PROGRESS,
        notificationPercent = arrayListOf(),
        percent = 2f
    )
    private val monthProgress = dayProgress.copy(
        id = 1234L,
        end = Date(System.currentTimeMillis()),
        start = Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS)),
        progressType = ProgressType.MONTH_PROGRESS
    )
    private val yearProgress = dayProgress.copy(
        id = 12356L,
        end = Date(System.currentTimeMillis()),
        start = Date(
            System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                365,
                TimeUnit.DAYS
            )
        ),
        progressType = ProgressType.YEAR_PROGRESS
    )
    private val customProgress = dayProgress.copy(
        id = 3455L,
        end = Date(System.currentTimeMillis()),
        start = Date(
            System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                2,
                TimeUnit.MINUTES
            )
        ),
        progressType = ProgressType.CUSTOM
    )

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val rule1 = RxSchedulersOverrideRule()

    @Mock
    lateinit var application: Application
    @Mock
    lateinit var resources: Resources
    @Mock
    lateinit var progressRepo: ProgressRepo
    @Mock
    lateinit var sharedPrefsProvider: SharedPrefsProvider
    @Mock
    lateinit var billingRepo: BillingRepo

    @Mock
    lateinit var appShortcutHelper: AppShortcutHelper

    @Mock
    lateinit var progressesObserver: Observer<ArrayList<ListItemRepresentable>>

    @Captor
    lateinit var progressesCaptor: ArgumentCaptor<ArrayList<ListItemRepresentable>>

    @Mock
    lateinit var askForRatingSignalObserver: Observer<Unit>
    @Mock
    lateinit var showInterstitialAdSignalObserver: Observer<Unit>
    @Mock
    lateinit var userMessagesObserver: Observer<String>
    @Captor
    lateinit var userMessagesCaptor: ArgumentCaptor<String>
    @Mock
    lateinit var expandProgressObserver: Observer<Long>
    @Captor
    lateinit var expandProgressCaptor: ArgumentCaptor<Long>

    private val progressListSubject = PublishSubject.create<List<Progress>>()
    private val isPurchasedSubject = BehaviorSubject.create<Boolean>()

    private lateinit var model: DashboardViewModel

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(progressRepo.observeAllProgress())
            .thenReturn(progressListSubject.hide().toFlowable(BackpressureStrategy.DROP))
        Mockito.`when`(billingRepo.observeIsPurchased()).thenReturn(isPurchasedSubject)
        Mockito.`when`(appShortcutHelper.updateDynamicShortcuts(anyList())).thenReturn(true)

        Mockito.`when`(resources.getDimension(anyInt())).thenReturn(2f)
        Mockito.`when`(application.resources).thenReturn(resources)
        Mockito.`when`(application.getString(anyInt(), anyFloat())).thenReturn("55%")
        Mockito.`when`(application.getString(R.string.dashboard_no_progress_message))
            .thenReturn(noItemsMessage)
        Mockito.`when`(application.getString(R.string.dashboard_error_loading_progress))
            .thenReturn(progressMonitorErrorMessage)
        Mockito.`when`(application.getString(R.string.error_progress_not_exist))
            .thenReturn(progressNotFoundMessage)

        model = DashboardViewModel(
            application,
            progressRepo,
            sharedPrefsProvider,
            billingRepo,
            appShortcutHelper
        )
        model.progresses.observeForever(progressesObserver)
        model.askForRatingSignal.observeForever(askForRatingSignalObserver)
        model.showInterstitialAdSignal.observeForever(showInterstitialAdSignalObserver)
        model.userMessages.observeForever(userMessagesObserver)
        model.expandProgress.observeForever(expandProgressObserver)
    }

    @After
    fun clear() {
        model.progresses.removeObserver(progressesObserver)
        model.askForRatingSignal.removeObserver(askForRatingSignalObserver)
        model.showInterstitialAdSignal.removeObserver(showInterstitialAdSignalObserver)
        model.userMessages.removeObserver(userMessagesObserver)
        model.expandProgress.removeObserver(expandProgressObserver)
    }

    @Test
    fun checkYipItemRepresentableList_WithProgressListAndProUser() {
        // Set
        val testProgressList = listOf(dayProgress, monthProgress, yearProgress, customProgress)
        progressListSubject.onNext(testProgressList)
        isPurchasedSubject.onNext(true)

        // Check
        Mockito.verify(progressesObserver, times(1 + INITIAL_TIMES))
            .onChanged(progressesCaptor.capture())
        assertEquals(
            dayProgress.id,
            (progressesCaptor.value[0] as ProgressListItem).progress.id
        )
        assertEquals(
            monthProgress.id,
            (progressesCaptor.value[1] as ProgressListItem).progress.id
        )
        assertEquals(
            yearProgress.id,
            (progressesCaptor.value[2] as ProgressListItem).progress.id
        )
        assertEquals(
            customProgress.id,
            (progressesCaptor.value[3] as ProgressListItem).progress.id
        )
        assertTrue(progressesCaptor.value[4] is PaddingItem)
    }

    @Test
    fun checkYipItemRepresentableList_WithProgressListAndNonProUser() {
        // Set
        val testProgressList = listOf(dayProgress, monthProgress, yearProgress, customProgress)
        progressListSubject.onNext(testProgressList)
        isPurchasedSubject.onNext(false)

        // Check
        Mockito.verify(progressesObserver, times(1 + INITIAL_TIMES))
            .onChanged(progressesCaptor.capture())
        assertEquals(
            dayProgress.id,
            (progressesCaptor.value[0] as ProgressListItem).progress.id
        )
        assertEquals(
            monthProgress.id,
            (progressesCaptor.value[1] as ProgressListItem).progress.id
        )
        assertEquals(
            yearProgress.id,
            (progressesCaptor.value[2] as ProgressListItem).progress.id
        )
        assertEquals(
            customProgress.id,
            (progressesCaptor.value[3] as ProgressListItem).progress.id
        )
        assertTrue(progressesCaptor.value[4] is AdsItem)
        assertTrue(progressesCaptor.value[5] is PaddingItem)
    }

    @Test
    fun checkYipItemRepresentableList_WithEmptyProgressListAndNonProUser() {
        // Set
        val testProgressList = listOf<Progress>()
        progressListSubject.onNext(testProgressList)
        isPurchasedSubject.onNext(false)

        // Check
        Mockito.verify(progressesObserver, times(1 + INITIAL_TIMES))
            .onChanged(progressesCaptor.capture())
        assertTrue(progressesCaptor.value[0] is EmptyRepresentable)
    }

    @Test
    fun checkDynamicShortcutsUpdated_WithProgressListAndNonProUser() {
        // Set
        val testProgressList = listOf(dayProgress, monthProgress, yearProgress, customProgress)
        progressListSubject.onNext(testProgressList)
        isPurchasedSubject.onNext(false)

        // Check
        Mockito.verify(appShortcutHelper, times(1))
            .updateDynamicShortcuts(testProgressList)
    }

    @Test
    fun checkYipItemRepresentableList_WithListWithTwoProgressAndNonProUser() {
        // Set
        val testProgressList = listOf(dayProgress, monthProgress)
        progressListSubject.onNext(testProgressList)
        isPurchasedSubject.onNext(false)

        // Check
        Mockito.verify(progressesObserver, times(1 + INITIAL_TIMES))
            .onChanged(progressesCaptor.capture())
        assertEquals(
            dayProgress.id,
            (progressesCaptor.value[0] as ProgressListItem).progress.id
        )
        assertEquals(
            monthProgress.id,
            (progressesCaptor.value[1] as ProgressListItem).progress.id
        )
        assertTrue(progressesCaptor.value[2] is AdsItem)
        assertTrue(progressesCaptor.value[3] is PaddingItem)
    }

    @Test
    fun checkExpandedProcessIdAfterProgressDeleted_WithProgressListAndNonProUser() {
        // Set
        val testProgressList = listOf(dayProgress, monthProgress, yearProgress)
        progressListSubject.onNext(testProgressList)
        isPurchasedSubject.onNext(false)

        // Check
        Mockito.verify(expandProgressObserver, times(1 + INITIAL_TIMES))
            .onChanged(expandProgressCaptor.capture())
        assertEquals(DashboardViewModel.RESET_COLLAPSED_ID, expandProgressCaptor.value)
    }

    @Test
    fun checkYipItemRepresentableList_WhenMonitorProgressFails() {
        // Set
        progressListSubject.onError(Throwable(progressMonitorErrorMessage))
        isPurchasedSubject.onNext(false)

        // Check
        Mockito.verify(progressesObserver, times(1 + INITIAL_TIMES))
            .onChanged(progressesCaptor.capture())
        assertTrue(progressesCaptor.value.firstOrNull() is ErrorRepresentable)
    }

    @Test
    fun checkInitialExpandProgressId() {
        assertEquals(DashboardViewModel.RESET_COLLAPSED_ID, model.expandProgress.value)
    }

    @Test
    fun checkResetExpandedProgress() {
        model.resetExpandedProgress()

        // Check
        Mockito.verify(expandProgressObserver, times(1 + INITIAL_TIMES))
            .onChanged(expandProgressCaptor.capture())
        assertEquals(DashboardViewModel.RESET_COLLAPSED_ID, expandProgressCaptor.value)
    }

    @Test
    fun checkIsDetailExpanded_whenProgressExpanded() {
        // Set
        Mockito.`when`(progressRepo.isProgressExist(anyLong())).thenReturn(Single.just(true))
        model.userWantsToOpenDetail(dayProgress.id)

        // Check
        assertTrue(model.isDetailExpanded())
    }

    @Test
    fun checkIsDetailExpanded_whenNoProgressExpanded() {
        // Set
        model.resetExpandedProgress()

        // Check
        assertFalse(model.isDetailExpanded())
    }

    @Test
    fun checkUserMessageAndExpandedId_whenOpenProgressDetail_givenProgressExists() {
        // Set
        Mockito.`when`(progressRepo.isProgressExist(anyLong())).thenReturn(Single.just(true))
        model.userWantsToOpenDetail(dayProgress.id)

        // Check
        Mockito.verify(expandProgressObserver, times(1 + INITIAL_TIMES))
            .onChanged(expandProgressCaptor.capture())
        assertEquals(dayProgress.id, expandProgressCaptor.value)
    }

    @Test
    fun checkUserMessageAndExpandedId_whenOpenProgressDetail_givenProgressNotExist() {
        // Set
        Mockito.`when`(progressRepo.isProgressExist(anyLong())).thenReturn(Single.just(false))
        model.userWantsToOpenDetail(dayProgress.id)

        // Check
        Mockito.verify(expandProgressObserver, times(INITIAL_TIMES))
            .onChanged(expandProgressCaptor.capture())
        assertEquals(DashboardViewModel.RESET_COLLAPSED_ID, expandProgressCaptor.value)

        Mockito.verify(userMessagesObserver, times(1)).onChanged(userMessagesCaptor.capture())
        assertEquals(progressNotFoundMessage, userMessagesCaptor.value)
    }

    @Test
    fun checkUserMessage_whenOpenProgressDetail_givenErrorToFindProgressExists() {
        // Set
        Mockito.`when`(progressRepo.isProgressExist(anyLong()))
            .thenReturn(Single.error(Throwable(progressNotFoundMessage)))
        model.userWantsToOpenDetail(dayProgress.id)

        // Check
        Mockito.verify(userMessagesObserver, times(1)).onChanged(userMessagesCaptor.capture())
        assertEquals(progressNotFoundMessage, userMessagesCaptor.value)
    }

    @Test
    fun checkPreferences_UserWantsToRateNow() {
        model.userWantsToRateNow()

        Mockito.verify(sharedPrefsProvider, times(1))
            .savePreferences(DashboardViewModel.PREF_KEY_NEVER_ASK_RATE, true)
    }

    @Test
    fun checkPreferences_UserWantsToNeverRate() {
        model.userWantsToNeverRate()

        Mockito.verify(sharedPrefsProvider, times(1))
            .savePreferences(DashboardViewModel.PREF_KEY_NEVER_ASK_RATE, true)
    }

    @Test
    fun checkAskForRatingSignal_whenUserOpensDetailsWithRandomInt_givenAlwaysAllowRatingAndProcessExist() {
        // Given
        Mockito.`when`(progressRepo.isProgressExist(anyLong())).thenReturn(Single.just(true))
        Mockito.`when`(
            sharedPrefsProvider.getBoolFromPreference(
                DashboardViewModel.PREF_KEY_NEVER_ASK_RATE,
                false
            )
        ).thenReturn(false)

        // When
        model.userWantsToOpenDetail(customProgress.id, DashboardViewModel.RANDOM_NUMBER_FOR_RATING)

        // Check
        Mockito.verify(askForRatingSignalObserver, times(1)).onChanged(Unit)
        Mockito.verify(showInterstitialAdSignalObserver, never()).onChanged(Unit)
    }

    @Test
    fun checkAskForRatingSignal_whenUserOpensDetailsWithRandomInt_givenNeverAllowRatingAndProcessExist() {
        // Given
        Mockito.`when`(progressRepo.isProgressExist(anyLong())).thenReturn(Single.just(true))
        Mockito.`when`(
            sharedPrefsProvider.getBoolFromPreference(
                DashboardViewModel.PREF_KEY_NEVER_ASK_RATE,
                false
            )
        ).thenReturn(true)

        // When
        model.userWantsToOpenDetail(customProgress.id, DashboardViewModel.RANDOM_NUMBER_FOR_RATING)

        // Check
        Mockito.verify(askForRatingSignalObserver, never()).onChanged(Unit)
        Mockito.verify(showInterstitialAdSignalObserver, never()).onChanged(Unit)
    }

    @Test
    fun checkAskForRatingSignal_whenUserOpensDetailsWithRandomInt_givenProgressNotExist() {
        // Set
        Mockito.`when`(progressRepo.isProgressExist(anyLong())).thenReturn(Single.just(false))
        model.userWantsToOpenDetail(dayProgress.id, DashboardViewModel.RANDOM_NUMBER_FOR_RATING)

        // Check
        Mockito.verify(askForRatingSignalObserver, never()).onChanged(Unit)
        Mockito.verify(showInterstitialAdSignalObserver, never()).onChanged(Unit)
    }

    @Test
    fun checkShowAdSignal_whenUserOpensDetailsWithRandomInt_givenUserIsProAndProcessExist() {
        // Given
        Mockito.`when`(progressRepo.isProgressExist(anyLong())).thenReturn(Single.just(true))
        Mockito.`when`(billingRepo.isPurchased()).thenReturn(true)


        // When
        model.userWantsToOpenDetail(customProgress.id, DashboardViewModel.RANDOM_NUMBER_FOR_AD)

        // Check
        Mockito.verify(askForRatingSignalObserver, never()).onChanged(Unit)
        Mockito.verify(showInterstitialAdSignalObserver, never()).onChanged(Unit)
    }

    @Test
    fun checkShowAdSignal_whenUserOpensDetailsWithRandomInt_givenUserIsNotProAndProcessExist() {
        // Given
        Mockito.`when`(progressRepo.isProgressExist(anyLong())).thenReturn(Single.just(true))
        Mockito.`when`(billingRepo.isPurchased()).thenReturn(false)

        // When
        model.userWantsToOpenDetail(customProgress.id, DashboardViewModel.RANDOM_NUMBER_FOR_AD)

        // Check
        Mockito.verify(askForRatingSignalObserver, never()).onChanged(Unit)
        Mockito.verify(showInterstitialAdSignalObserver, times(1)).onChanged(Unit)
    }

    @Test
    fun checkShowAdSignal_whenUserOpensDetailsWithRandomInt_givenProgressNotExist() {
        // Set
        Mockito.`when`(progressRepo.isProgressExist(anyLong())).thenReturn(Single.just(false))
        model.userWantsToOpenDetail(dayProgress.id, DashboardViewModel.RANDOM_NUMBER_FOR_AD)

        // Check
        Mockito.verify(askForRatingSignalObserver, never()).onChanged(Unit)
        Mockito.verify(showInterstitialAdSignalObserver, never()).onChanged(Unit)
    }

    companion object {
        private const val INITIAL_TIMES = 1
    }
}
