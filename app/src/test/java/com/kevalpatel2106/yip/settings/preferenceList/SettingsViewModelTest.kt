package com.kevalpatel2106.yip.settings.preferenceList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.testutils.RxSchedulersOverrideRule
import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.nightModeRepo.NightModeRepo
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class SettingsViewModelTest {

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val rule1: TestRule = RxSchedulersOverrideRule()

    @Mock
    internal lateinit var billingRepo: BillingRepo

    @Mock
    internal lateinit var nightModeRepo: NightModeRepo

    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private val darkModeObservable = BehaviorSubject.create<Int>()
    private val isPurchasedObservable = BehaviorSubject.create<Boolean>()
    private val initialDarkModeValue = kFixture<Int>()
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this@SettingsViewModelTest)
        whenever(billingRepo.observeIsPurchased()).thenReturn(isPurchasedObservable)
        whenever(nightModeRepo.observeNightModeChanges()).thenReturn(darkModeObservable)
        whenever(nightModeRepo.getNightModeSetting()).thenReturn(initialDarkModeValue)

        viewModel = SettingsViewModel(nightModeRepo, billingRepo)
    }

    @Test
    fun `when view model initialized check refresh purchase state called`() {
        // Check
        verify(billingRepo).refreshPurchaseState()
    }

    @Test
    fun `when view model initialized check initial view state value`() {
        // Check
        assertEquals(
            SettingsFragmentViewState.initialState(initialDarkModeValue),
            viewModel.viewState.getOrAwaitValue()
        )
    }

    @Test
    fun `when subscribe to user pro status changes check buy pro button is disable`() {

        // Check
        val viewState = viewModel.viewState.getOrAwaitValue()
        assertFalse(viewState.isBuyProClickable)
    }

    @Test
    fun `when user pro status changes check buy pro button is enabled`() {
        // When
        isPurchasedObservable.onNext(kFixture())

        // Check
        val viewState = viewModel.viewState.getOrAwaitValue()
        assertTrue(viewState.isBuyProClickable)
    }

    @Test
    fun `when user pro status changes to pro check buy pro button is invisible`() {
        // When
        isPurchasedObservable.onNext(true)

        // Check
        val viewState = viewModel.viewState.getOrAwaitValue()
        assertFalse(viewState.isBuyProVisible)
    }

    @Test
    fun `when user pro status changes to not pro check buy pro button is visible`() {
        // When
        isPurchasedObservable.onNext(false)

        // Check
        val viewState = viewModel.viewState.getOrAwaitValue()
        assertTrue(viewState.isBuyProVisible)
    }

    @Test
    fun `when dark mode settings changes check dark mode value in view state updates`() {
        // given
        val newDarkModeValue = kFixture<Int>()

        // When
        darkModeObservable.onNext(newDarkModeValue)

        // Check
        val viewState = viewModel.viewState.getOrAwaitValue()
        assertEquals(newDarkModeValue, viewState.darkModeValue)
    }
}
