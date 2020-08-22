package com.kevalpatel2106.yip.edit

import android.app.Application
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.testutils.RxSchedulersOverrideRule
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatter
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.repo.validator.Validator
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.MockitoAnnotations

internal abstract class EditViewDeadlineModelTestSetUp {

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val rule1 = RxSchedulersOverrideRule()

    @Mock
    lateinit var context: Application

    @Mock
    lateinit var deadlineRepo: DeadlineRepo

    @Mock
    lateinit var alarmRepo: AlarmRepo

    @Mock
    lateinit var billingRepo: BillingRepo

    @Mock
    lateinit var resources: Resources

    @Mock
    internal lateinit var validator: Validator

    @Mock
    lateinit var dateFormatter: DateFormatter

    protected val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    protected val testString = kFixture<String>()
    protected val dateFormattedString = kFixture<String>()
    protected val userPremiumStatus = BehaviorSubject.create<Boolean>()
    protected val deadlineSubject = BehaviorSubject.create<Deadline>()
    protected lateinit var viewModel: EditDeadlineViewModel

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        whenever(billingRepo.observeIsPurchased()).thenReturn(BehaviorSubject.create())
        whenever(context.resources).thenReturn(resources)
        whenever(context.getString(anyInt())).thenReturn(testString)
        whenever(context.getString(anyInt(), anyInt())).thenReturn(testString)
        whenever(resources.getInteger(anyInt())).thenReturn(kFixture.range(10..20))

        whenever(billingRepo.observeIsPurchased()).thenReturn(userPremiumStatus)
        whenever(dateFormatter.formatDateOnly(any())).thenReturn(dateFormattedString)
        whenever(deadlineRepo.observeDeadline(any()))
            .thenReturn(deadlineSubject.toFlowable(BackpressureStrategy.DROP))

        viewModel = EditDeadlineViewModel(
            context,
            deadlineRepo,
            alarmRepo,
            validator,
            dateFormatter,
            billingRepo
        )
    }
}
