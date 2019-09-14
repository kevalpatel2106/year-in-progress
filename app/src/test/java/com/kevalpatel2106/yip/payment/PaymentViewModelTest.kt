package com.kevalpatel2106.yip.payment

import android.app.Activity
import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.emptyString
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class PaymentViewModelTest {

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var billingRepo: BillingRepo

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var activity: Activity

    @Mock
    internal lateinit var viewStateObserver: Observer<PaymentActivityViewState>

    @Mock
    internal lateinit var userMessageObserver: Observer<String>

    @Mock
    internal lateinit var closeSignalObserver: Observer<Unit>

    @Captor
    internal lateinit var userMessageCaptor: ArgumentCaptor<String>

    @Captor
    internal lateinit var viewStateCaptor: ArgumentCaptor<PaymentActivityViewState>

    private lateinit var paymentViewModel: PaymentViewModel
    private val testSuccessMessage = "Good!"

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(application.getString(R.string.purchase_successful))
            .thenReturn(testSuccessMessage)

        paymentViewModel = PaymentViewModel(application, billingRepo)
        paymentViewModel.userMessage.observeForever(userMessageObserver)
        paymentViewModel.viewState.observeForever(viewStateObserver)
        paymentViewModel.closeSignal.observeForever(closeSignalObserver)
    }


    @After
    fun after() {
        paymentViewModel.viewState.removeObserver(viewStateObserver)
        paymentViewModel.userMessage.removeObserver(userMessageObserver)
        paymentViewModel.closeSignal.removeObserver(closeSignalObserver)
    }

    @Test
    fun checkInitialViewState() {
        Mockito.verify(viewStateObserver, Mockito.times(INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        assertNotNull(paymentViewModel.viewState.value)
        assertEquals(PaymentActivityViewState.initialState(), paymentViewModel.viewState.value)
        assertEquals(PaymentActivityViewState.initialState(), viewStateCaptor.value)
    }

    @Test
    fun checkViewStatesWhenPurchaseSuccess() {
        // Set up
        Mockito.`when`(billingRepo.purchase(any(), anyString()))
            .thenReturn(Single.just(emptyString()))

        paymentViewModel.purchase(activity)

        // Verify
        Mockito.verify(billingRepo, Mockito.times(1)).purchase(activity, BillingRepo.SKU_ID)

        Mockito.verify(viewStateObserver, Mockito.times(2 + INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        assertFalse(viewStateCaptor.allValues[1].upgradeButtonClickable)
        assertTrue(viewStateCaptor.value.upgradeButtonClickable)

        Mockito.verify(userMessageObserver, Mockito.times(1)).onChanged(userMessageCaptor.capture())
        assertEquals(testSuccessMessage, userMessageCaptor.value)
        Mockito.verify(closeSignalObserver, Mockito.times(1)).onChanged(Unit)
    }

    @Test
    fun checkViewStatesWhenPurchaseError() {
        // Set up
        val errorMessage = "Error!"
        Mockito.`when`(billingRepo.purchase(any(), anyString()))
            .thenReturn(Single.error(Throwable(errorMessage)))

        paymentViewModel.purchase(activity)

        // Verify
        Mockito.verify(billingRepo, Mockito.times(1)).purchase(activity, BillingRepo.SKU_ID)

        Mockito.verify(viewStateObserver, Mockito.times(2 + INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        assertFalse(viewStateCaptor.allValues[1].upgradeButtonClickable)
        assertTrue(viewStateCaptor.value.upgradeButtonClickable)

        Mockito.verify(userMessageObserver, Mockito.times(1)).onChanged(errorMessage)
        Mockito.verify(closeSignalObserver, Mockito.never()).onChanged(Unit)
    }

    companion object {
        private const val INITIAL_STATE_ON_CHANGE = 1
    }
}
