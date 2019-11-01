package com.kevalpatel2106.yip.dashboard

import android.app.Application
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kevalpatel2106.testutils.RxSchedulersOverrideRule
import com.kevalpatel2106.yip.core.recyclerview.representable.YipItemRepresentable
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.progressesRepo.ProgressRepo
import com.kevalpatel2106.yip.repo.utils.SharedPrefsProvider
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyFloat
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class DashboardViewModelTest {
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
    private val testProgressList = listOf(dayProgress, monthProgress, yearProgress, customProgress)

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
    lateinit var progressesObserver: Observer<ArrayList<YipItemRepresentable>>
    @Captor
    lateinit var progressesCaptor: ArgumentCaptor<ArrayList<YipItemRepresentable>>
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
    fun checkWithProgressListAndProUser() {
        progressListSubject.onNext(testProgressList)
        isPurchasedSubject.onNext(true)
        // TODO finish and add more tests
    }
}
