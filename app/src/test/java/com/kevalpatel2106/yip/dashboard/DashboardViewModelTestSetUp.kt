package com.kevalpatel2106.yip.dashboard

import android.app.Application
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.testutils.RxSchedulersOverrideRule
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.repo.utils.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.anyFloat
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

abstract class DashboardViewModelTestSetUp {
    protected val deadlineMonitorErrorMessage = "test error"
    protected val deadlineNotFoundMessage = "test error 1"
    protected val noItemsMessage = "noItems"

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

    protected val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    protected val deadlineListSubject = PublishSubject.create<List<Deadline>>()
    protected val isPurchasedSubject = BehaviorSubject.create<Boolean>()

    internal lateinit var model: DashboardViewModel

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
    }

    protected fun setAsDeadlineDetailExpanded(testExpandedDeadline: Long) {
        Mockito.`when`(deadlineRepo.isDeadlineExist(testExpandedDeadline))
            .thenReturn(Single.just(true))
        model.onOpenDeadlineDetail(testExpandedDeadline)
    }
}
