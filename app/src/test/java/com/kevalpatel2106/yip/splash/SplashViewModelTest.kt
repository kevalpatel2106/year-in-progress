package com.kevalpatel2106.yip.splash

import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class SplashViewModelTest {

    @Mock
    internal lateinit var billingRepo: BillingRepo

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this@SplashViewModelTest)
    }

    @Test
    fun `when view model initialised check refreshing purchase state`() {
        val viewModel = SplashViewModel(billingRepo)
        viewModel.refreshPurchase()

        verify(billingRepo).refreshPurchaseState(BillingRepo.SKU_ID)
    }
}
