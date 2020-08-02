package com.kevalpatel2106.yip.splash

import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.nightModeRepo.NightModeRepo
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

    @Mock
    internal lateinit var nightModeRepo: NightModeRepo

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this@SplashViewModelTest)
    }

    @Test
    fun `when view model initialised check refreshing purchase state`() {
        SplashViewModel(billingRepo, nightModeRepo)

        verify(billingRepo).refreshPurchaseState(BillingRepo.SKU_ID)
    }

    @Test
    fun `when view model initialised check night mode is set`() {
        SplashViewModel(billingRepo, nightModeRepo)

        verify(nightModeRepo).getNightModeSetting()
    }
}
