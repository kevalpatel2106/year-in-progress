package com.kevalpatel2106.yip.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kevalpatel2106.yip.BuildConfig
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert
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
class SettingsViewModelTest {

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    internal lateinit var billingRepo: BillingRepo

    private val isPurchasedObservable: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(true)

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this@SettingsViewModelTest)
        Mockito.`when`(billingRepo.observeIsPurchased()).thenReturn(isPurchasedObservable)
    }

    @Test
    fun checkInitWhenProUser() {
        val viewModel = SettingsViewModel(billingRepo)
        Mockito.verify(billingRepo, Mockito.atLeast(1)).observeIsPurchased()

        isPurchasedObservable.onNext(true)
        Assert.assertEquals(true, viewModel.viewState.value?.isBuyProClickable)
        Assert.assertEquals(false, viewModel.viewState.value?.isBuyProVisible)
        Assert.assertEquals(
            BuildConfig.VERSION_NAME,
            viewModel.viewState.value?.versionPreferenceSummary
        )
    }

    @Test
    fun checkInitWhenNonProUser() {
        val viewModel = SettingsViewModel(billingRepo)
        Mockito.verify(billingRepo, Mockito.atLeast(1)).observeIsPurchased()

        isPurchasedObservable.onNext(false)
        Assert.assertEquals(true, viewModel.viewState.value?.isBuyProClickable)
        Assert.assertEquals(true, viewModel.viewState.value?.isBuyProVisible)
        Assert.assertEquals(
            BuildConfig.VERSION_NAME,
            viewModel.viewState.value?.versionPreferenceSummary
        )
    }
}
