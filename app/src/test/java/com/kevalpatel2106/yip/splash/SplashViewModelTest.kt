package com.kevalpatel2106.yip.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class SplashViewModelTest {

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    internal lateinit var billingRepo: BillingRepo

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this@SplashViewModelTest)
    }

    @Test
    fun checkRefreshPurchaseState() {
        val viewModel = SplashViewModel(billingRepo)
        viewModel.refreshPurchase()

        Mockito.verify(billingRepo).refreshPurchaseState(BillingRepo.SKU_ID)
    }
}
