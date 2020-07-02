package com.kevalpatel2106.yip.dashboard

import android.app.Application
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kevalpatel2106.testutils.RxSchedulersOverrideRule
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.dashboard.adapter.listItem.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.DeadlineListItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.EmptyRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ErrorRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ListItemRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.PaddingItem
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
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
    private val deadlineMonitorErrorMessage = "test error"
    private val deadlineNotFoundMessage = "test error 1"
    private val noItemsMessage = "noItems"
    private val dayDeadline = Deadline(
        id = 38465L,
        title = "Test title",
        color = DeadlineColor.COLOR_YELLOW,
        end = Date(System.currentTimeMillis()),
        start = Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)),
        deadlineType = DeadlineType.DAY_DEADLINE,
        notificationPercent = arrayListOf(),
        percent = 2f
    )
    private val monthDeadline = dayDeadline.copy(
        id = 1234L,
        end = Date(System.currentTimeMillis()),
        start = Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS)),
        deadlineType = DeadlineType.MONTH_DEADLINE
    )
    private val yearDeadline = dayDeadline.copy(
        id = 12356L,
        end = Date(System.currentTimeMillis()),
        start = Date(
            System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                365,
                TimeUnit.DAYS
            )
        ),
        deadlineType = DeadlineType.YEAR_DEADLINE
    )
    private val customDeadline = dayDeadline.copy(
        id = 3455L,
        end = Date(System.currentTimeMillis()),
        start = Date(
            System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                2,
                TimeUnit.MINUTES
            )
        ),
        deadlineType = DeadlineType.CUSTOM
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
    lateinit var deadlineRepo: DeadlineRepo

    @Mock
    lateinit var sharedPrefsProvider: SharedPrefsProvider

    @Mock
    lateinit var billingRepo: BillingRepo

    @Mock
    lateinit var appShortcutHelper: AppShortcutHelper

    @Mock
    lateinit var deadlinesObserver: Observer<ArrayList<ListItemRepresentable>>

    @Captor
    lateinit var deadlinesCaptor: ArgumentCaptor<ArrayList<ListItemRepresentable>>

    @Mock
    lateinit var askForRatingSignalObserver: Observer<Unit>

    @Mock
    lateinit var showInterstitialAdSignalObserver: Observer<Unit>

    @Mock
    lateinit var userMessagesObserver: Observer<String>

    @Captor
    lateinit var userMessagesCaptor: ArgumentCaptor<String>

    @Mock
    lateinit var expandDeadlineObserver: Observer<Long>

    @Captor
    lateinit var expandDeadlineCaptor: ArgumentCaptor<Long>

    private val deadlineListSubject = PublishSubject.create<List<Deadline>>()
    private val isPurchasedSubject = BehaviorSubject.create<Boolean>()

    private lateinit var model: DashboardViewModel

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(deadlineRepo.observeAllDeadlines())
            .thenReturn(deadlineListSubject.hide().toFlowable(BackpressureStrategy.DROP))
        Mockito.`when`(billingRepo.observeIsPurchased()).thenReturn(isPurchasedSubject)
        Mockito.`when`(appShortcutHelper.updateDynamicShortcuts(anyList())).thenReturn(true)

        Mockito.`when`(resources.getDimension(anyInt())).thenReturn(2f)
        Mockito.`when`(application.resources).thenReturn(resources)
        Mockito.`when`(application.getString(anyInt(), anyFloat())).thenReturn("55%")
        Mockito.`when`(application.getString(R.string.dashboard_no_deadline_message))
            .thenReturn(noItemsMessage)
        Mockito.`when`(application.getString(R.string.dashboard_error_loading_deadline))
            .thenReturn(deadlineMonitorErrorMessage)
        Mockito.`when`(application.getString(R.string.error_deadline_not_exist))
            .thenReturn(deadlineNotFoundMessage)

        model = DashboardViewModel(
            application,
            deadlineRepo,
            sharedPrefsProvider,
            billingRepo,
            appShortcutHelper
        )
        model.deadlines.observeForever(deadlinesObserver)
        model.askForRatingSignal.observeForever(askForRatingSignalObserver)
        model.showInterstitialAdSignal.observeForever(showInterstitialAdSignalObserver)
        model.userMessages.observeForever(userMessagesObserver)
        model.expandDeadline.observeForever(expandDeadlineObserver)
    }

    @After
    fun clear() {
        model.deadlines.removeObserver(deadlinesObserver)
        model.askForRatingSignal.removeObserver(askForRatingSignalObserver)
        model.showInterstitialAdSignal.removeObserver(showInterstitialAdSignalObserver)
        model.userMessages.removeObserver(userMessagesObserver)
        model.expandDeadline.removeObserver(expandDeadlineObserver)
    }

    @Test
    fun checkYipItemRepresentableList_WithDeadlinesListAndProUser() {
        // Set
        val testDeadlines = listOf(dayDeadline, monthDeadline, yearDeadline, customDeadline)
        deadlineListSubject.onNext(testDeadlines)
        isPurchasedSubject.onNext(true)

        // Check
        Mockito.verify(deadlinesObserver, times(1 + INITIAL_TIMES))
            .onChanged(deadlinesCaptor.capture())
        assertEquals(
            dayDeadline.id,
            (deadlinesCaptor.value[0] as DeadlineListItem).deadline.id
        )
        assertEquals(
            monthDeadline.id,
            (deadlinesCaptor.value[1] as DeadlineListItem).deadline.id
        )
        assertEquals(
            yearDeadline.id,
            (deadlinesCaptor.value[2] as DeadlineListItem).deadline.id
        )
        assertEquals(
            customDeadline.id,
            (deadlinesCaptor.value[3] as DeadlineListItem).deadline.id
        )
        assertTrue(deadlinesCaptor.value[4] is PaddingItem)
    }

    @Test
    fun checkYipItemRepresentableList_WithDeadlineListAndNonProUser() {
        // Set
        val testDeadlines = listOf(dayDeadline, monthDeadline, yearDeadline, customDeadline)
        deadlineListSubject.onNext(testDeadlines)
        isPurchasedSubject.onNext(false)

        // Check
        Mockito.verify(deadlinesObserver, times(1 + INITIAL_TIMES))
            .onChanged(deadlinesCaptor.capture())
        assertEquals(
            dayDeadline.id,
            (deadlinesCaptor.value[0] as DeadlineListItem).deadline.id
        )
        assertEquals(
            monthDeadline.id,
            (deadlinesCaptor.value[1] as DeadlineListItem).deadline.id
        )
        assertEquals(
            yearDeadline.id,
            (deadlinesCaptor.value[2] as DeadlineListItem).deadline.id
        )
        assertEquals(
            customDeadline.id,
            (deadlinesCaptor.value[3] as DeadlineListItem).deadline.id
        )
        assertTrue(deadlinesCaptor.value[4] is AdsItem)
        assertTrue(deadlinesCaptor.value[5] is PaddingItem)
    }

    @Test
    fun checkYipItemRepresentableList_WithEmptyDeadlineListAndNonProUser() {
        // Set
        val testDeadlines = listOf<Deadline>()
        deadlineListSubject.onNext(testDeadlines)
        isPurchasedSubject.onNext(false)

        // Check
        Mockito.verify(deadlinesObserver, times(1 + INITIAL_TIMES))
            .onChanged(deadlinesCaptor.capture())
        assertTrue(deadlinesCaptor.value[0] is EmptyRepresentable)
    }

    @Test
    fun checkDynamicShortcutsUpdated_WithDeadlineListAndNonProUser() {
        // Set
        val testDeadlines = listOf(dayDeadline, monthDeadline, yearDeadline, customDeadline)
        deadlineListSubject.onNext(testDeadlines)
        isPurchasedSubject.onNext(false)

        // Check
        Mockito.verify(appShortcutHelper, times(1))
            .updateDynamicShortcuts(testDeadlines)
    }

    @Test
    fun checkYipItemRepresentableList_WithListWithTwoDeadlineAndNonProUser() {
        // Set
        val testDeadlines = listOf(dayDeadline, monthDeadline)
        deadlineListSubject.onNext(testDeadlines)
        isPurchasedSubject.onNext(false)

        // Check
        Mockito.verify(deadlinesObserver, times(1 + INITIAL_TIMES))
            .onChanged(deadlinesCaptor.capture())
        assertEquals(
            dayDeadline.id,
            (deadlinesCaptor.value[0] as DeadlineListItem).deadline.id
        )
        assertEquals(
            monthDeadline.id,
            (deadlinesCaptor.value[1] as DeadlineListItem).deadline.id
        )
        assertTrue(deadlinesCaptor.value[2] is AdsItem)
        assertTrue(deadlinesCaptor.value[3] is PaddingItem)
    }

    @Test
    fun checkExpandedProcessIdAfterDeadlineDeleted_WithDeadlineListAndNonProUser() {
        // Set
        val testDeadlines = listOf(dayDeadline, monthDeadline, yearDeadline)
        deadlineListSubject.onNext(testDeadlines)
        isPurchasedSubject.onNext(false)

        // Check
        Mockito.verify(expandDeadlineObserver, times(1 + INITIAL_TIMES))
            .onChanged(expandDeadlineCaptor.capture())
        assertEquals(DashboardViewModel.RESET_COLLAPSED_ID, expandDeadlineCaptor.value)
    }

    @Test
    fun checkYipItemRepresentableList_WhenMonitorDeadlineFails() {
        // Set
        deadlineListSubject.onError(Throwable(deadlineMonitorErrorMessage))
        isPurchasedSubject.onNext(false)

        // Check
        Mockito.verify(deadlinesObserver, times(1 + INITIAL_TIMES))
            .onChanged(deadlinesCaptor.capture())
        assertTrue(deadlinesCaptor.value.firstOrNull() is ErrorRepresentable)
    }

    @Test
    fun checkInitialExpandDeadlineId() {
        assertEquals(DashboardViewModel.RESET_COLLAPSED_ID, model.expandDeadline.value)
    }

    @Test
    fun checkResetExpandedDeadline() {
        model.resetExpandedDeadline()

        // Check
        Mockito.verify(expandDeadlineObserver, times(1 + INITIAL_TIMES))
            .onChanged(expandDeadlineCaptor.capture())
        assertEquals(DashboardViewModel.RESET_COLLAPSED_ID, expandDeadlineCaptor.value)
    }

    @Test
    fun checkIsDetailExpanded_whenDeadlineExpanded() {
        // Set
        Mockito.`when`(deadlineRepo.isDeadlineExist(anyLong())).thenReturn(Single.just(true))
        model.userWantsToOpenDetail(dayDeadline.id)

        // Check
        assertTrue(model.isDetailExpanded())
    }

    @Test
    fun checkIsDetailExpanded_whenNoDeadlineExpanded() {
        // Set
        model.resetExpandedDeadline()

        // Check
        assertFalse(model.isDetailExpanded())
    }

    @Test
    fun checkUserMessageAndExpandedId_whenOpenDeadlineDetail_givenDeadlineExists() {
        // Set
        Mockito.`when`(deadlineRepo.isDeadlineExist(anyLong())).thenReturn(Single.just(true))
        model.userWantsToOpenDetail(dayDeadline.id)

        // Check
        Mockito.verify(expandDeadlineObserver, times(1 + INITIAL_TIMES))
            .onChanged(expandDeadlineCaptor.capture())
        assertEquals(dayDeadline.id, expandDeadlineCaptor.value)
    }

    @Test
    fun checkUserMessageAndExpandedId_whenOpenDeadlineDetail_givenDeadlineNotExist() {
        // Set
        Mockito.`when`(deadlineRepo.isDeadlineExist(anyLong())).thenReturn(Single.just(false))
        model.userWantsToOpenDetail(dayDeadline.id)

        // Check
        Mockito.verify(expandDeadlineObserver, times(INITIAL_TIMES))
            .onChanged(expandDeadlineCaptor.capture())
        assertEquals(DashboardViewModel.RESET_COLLAPSED_ID, expandDeadlineCaptor.value)

        Mockito.verify(userMessagesObserver, times(1)).onChanged(userMessagesCaptor.capture())
        assertEquals(deadlineNotFoundMessage, userMessagesCaptor.value)
    }

    @Test
    fun checkUserMessage_whenOpenDeadlineDetail_givenErrorToFindDeadlineExists() {
        // Set
        Mockito.`when`(deadlineRepo.isDeadlineExist(anyLong()))
            .thenReturn(Single.error(Throwable(deadlineNotFoundMessage)))
        model.userWantsToOpenDetail(dayDeadline.id)

        // Check
        Mockito.verify(userMessagesObserver, times(1)).onChanged(userMessagesCaptor.capture())
        assertEquals(deadlineNotFoundMessage, userMessagesCaptor.value)
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
        Mockito.`when`(deadlineRepo.isDeadlineExist(anyLong())).thenReturn(Single.just(true))
        Mockito.`when`(
            sharedPrefsProvider.getBoolFromPreference(
                DashboardViewModel.PREF_KEY_NEVER_ASK_RATE,
                false
            )
        ).thenReturn(false)

        // When
        model.userWantsToOpenDetail(customDeadline.id, DashboardViewModel.RANDOM_NUMBER_FOR_RATING)

        // Check
        Mockito.verify(askForRatingSignalObserver, times(1)).onChanged(Unit)
        Mockito.verify(showInterstitialAdSignalObserver, never()).onChanged(Unit)
    }

    @Test
    fun checkAskForRatingSignal_whenUserOpensDetailsWithRandomInt_givenNeverAllowRatingAndProcessExist() {
        // Given
        Mockito.`when`(deadlineRepo.isDeadlineExist(anyLong())).thenReturn(Single.just(true))
        Mockito.`when`(
            sharedPrefsProvider.getBoolFromPreference(
                DashboardViewModel.PREF_KEY_NEVER_ASK_RATE,
                false
            )
        ).thenReturn(true)

        // When
        model.userWantsToOpenDetail(customDeadline.id, DashboardViewModel.RANDOM_NUMBER_FOR_RATING)

        // Check
        Mockito.verify(askForRatingSignalObserver, never()).onChanged(Unit)
        Mockito.verify(showInterstitialAdSignalObserver, never()).onChanged(Unit)
    }

    @Test
    fun checkAskForRatingSignal_whenUserOpensDetailsWithRandomInt_givenDeadlineNotExist() {
        // Set
        Mockito.`when`(deadlineRepo.isDeadlineExist(anyLong())).thenReturn(Single.just(false))
        model.userWantsToOpenDetail(dayDeadline.id, DashboardViewModel.RANDOM_NUMBER_FOR_RATING)

        // Check
        Mockito.verify(askForRatingSignalObserver, never()).onChanged(Unit)
        Mockito.verify(showInterstitialAdSignalObserver, never()).onChanged(Unit)
    }

    @Test
    fun checkShowAdSignal_whenUserOpensDetailsWithRandomInt_givenUserIsProAndProcessExist() {
        // Given
        Mockito.`when`(deadlineRepo.isDeadlineExist(anyLong())).thenReturn(Single.just(true))
        Mockito.`when`(billingRepo.isPurchased()).thenReturn(true)


        // When
        model.userWantsToOpenDetail(customDeadline.id, DashboardViewModel.RANDOM_NUMBER_FOR_AD)

        // Check
        Mockito.verify(askForRatingSignalObserver, never()).onChanged(Unit)
        Mockito.verify(showInterstitialAdSignalObserver, never()).onChanged(Unit)
    }

    @Test
    fun checkShowAdSignal_whenUserOpensDetailsWithRandomInt_givenUserIsNotProAndProcessExist() {
        // Given
        Mockito.`when`(deadlineRepo.isDeadlineExist(anyLong())).thenReturn(Single.just(true))
        Mockito.`when`(billingRepo.isPurchased()).thenReturn(false)

        // When
        model.userWantsToOpenDetail(customDeadline.id, DashboardViewModel.RANDOM_NUMBER_FOR_AD)

        // Check
        Mockito.verify(askForRatingSignalObserver, never()).onChanged(Unit)
        Mockito.verify(showInterstitialAdSignalObserver, times(1)).onChanged(Unit)
    }

    @Test
    fun checkShowAdSignal_whenUserOpensDetailsWithRandomInt_givenDeadlineNotExist() {
        // Set
        Mockito.`when`(deadlineRepo.isDeadlineExist(anyLong())).thenReturn(Single.just(false))
        model.userWantsToOpenDetail(dayDeadline.id, DashboardViewModel.RANDOM_NUMBER_FOR_AD)

        // Check
        Mockito.verify(askForRatingSignalObserver, never()).onChanged(Unit)
        Mockito.verify(showInterstitialAdSignalObserver, never()).onChanged(Unit)
    }

    companion object {
        private const val INITIAL_TIMES = 1
    }
}
