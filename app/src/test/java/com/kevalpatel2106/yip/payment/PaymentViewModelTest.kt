package com.kevalpatel2106.yip.payment

import android.app.Activity
import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.testutils.RxSchedulersOverrideRule
import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class PaymentViewModelTest {

    @JvmField
    @Rule
    val rule1 = RxSchedulersOverrideRule()

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var billingRepo: BillingRepo

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var activity: Activity

    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private val testSuccessMessage = kFixture<String>()

    private lateinit var model: PaymentViewModel

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        whenever(application.getString(R.string.purchase_successful))
            .thenReturn(testSuccessMessage)

        model = PaymentViewModel(application, billingRepo)
    }

    @Test
    fun `when view model created check view state`() {
        val initialViewState = model.viewState.getOrAwaitValue()
        assertEquals(PaymentActivityViewState.initialState(), initialViewState)
    }

    @Test
    fun `given purchase success when purchase button clicked check success message shown`() {
        // given
        whenever(billingRepo.purchase(activity)).thenReturn(Single.just(kFixture()))

        // when
        model.purchase(activity)

        // check
        val singleEvent = model.singleEvent.getOrAwaitValue()
        assertEquals(testSuccessMessage, (singleEvent as ShowUserMessage).message)
    }

    @Test
    fun `given purchase success when purchase button clicked check screen closes`() {
        // given
        whenever(billingRepo.purchase(activity)).thenReturn(Single.just(kFixture()))

        // when
        model.purchase(activity)

        // check
        val singleEvent = model.singleEvent.getOrAwaitValue()
        assertTrue((singleEvent as ShowUserMessage).closeScreen)
    }

    @Test
    fun `given purchase fails when purchase button clicked check error message shown`() {
        // given
        val errorMessage = kFixture<String>()
        whenever(billingRepo.purchase(activity))
            .thenReturn(Single.error(Throwable(errorMessage)))

        // when
        model.purchase(activity)

        // check
        val singleEvent = model.singleEvent.getOrAwaitValue()
        assertEquals(errorMessage, (singleEvent as ShowUserMessage).message)
    }

    @Test
    fun `given purchase success when purchase button clicked check screen does not close`() {
        // given
        val errorMessage = kFixture<String>()
        whenever(billingRepo.purchase(activity))
            .thenReturn(Single.error(Throwable(errorMessage)))

        // when
        model.purchase(activity)

        // check
        val singleEvent = model.singleEvent.getOrAwaitValue()
        assertFalse((singleEvent as ShowUserMessage).closeScreen)
    }

    @Test
    fun `given purchase success when purchase button clicked check upgrade button clickable`() {
        // given
        val errorMessage = kFixture<String>()
        whenever(billingRepo.purchase(activity))
            .thenReturn(Single.error(Throwable(errorMessage)))

        // when
        model.purchase(activity)

        // check
        assertTrue(model.viewState.getOrAwaitValue().upgradeButtonClickable)
    }
}
