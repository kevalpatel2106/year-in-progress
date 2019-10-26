package com.kevalpatel2106.yip.settings.preferenceList

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kevalpatel2106.yip.BuildConfig
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.utils.SharedPrefsProvider
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
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
    private val testPrefKeyDarkMode = "dark_mode"
    private val testDarkModeString = "dark_mode_on" // Copied from const.xml

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    internal lateinit var billingRepo: BillingRepo

    @Mock
    internal lateinit var application: Application

    @Mock
    internal lateinit var sharedPrefsProvider: SharedPrefsProvider

    private val darkModePrefObserver = PublishSubject.create<String>()
    private val isPurchasedObservable = BehaviorSubject.createDefault(true)

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this@SettingsViewModelTest)

        Mockito.`when`(billingRepo.observeIsPurchased()).thenReturn(isPurchasedObservable)
        Mockito.`when`(application.getString(R.string.pref_key_dark_mode))
            .thenReturn(testPrefKeyDarkMode)
        Mockito.`when`(application.getString(R.string.dark_mode_on))
            .thenReturn(testDarkModeString)
        Mockito.`when`(sharedPrefsProvider.observeStringFromPreference(testPrefKeyDarkMode))
            .thenReturn(darkModePrefObserver.hide())
    }

    @Test
    fun checkInitWhenProUser() {
        // When
        val viewModel = SettingsViewModel(application, sharedPrefsProvider, billingRepo)

        // Check
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
        // When
        val viewModel = SettingsViewModel(application, sharedPrefsProvider, billingRepo)

        // Check
        Mockito.verify(billingRepo, Mockito.atLeast(1)).observeIsPurchased()

        isPurchasedObservable.onNext(false)
        Assert.assertEquals(true, viewModel.viewState.value?.isBuyProClickable)
        Assert.assertEquals(true, viewModel.viewState.value?.isBuyProVisible)
        Assert.assertEquals(
            BuildConfig.VERSION_NAME,
            viewModel.viewState.value?.versionPreferenceSummary
        )
    }

    @Test
    fun checkInitWhenDarkModeOn() {
        // When
        val viewModel = SettingsViewModel(application, sharedPrefsProvider, billingRepo)
        darkModePrefObserver.onNext(testDarkModeString)

        // Check
        Assert.assertEquals(
            AppCompatDelegate.MODE_NIGHT_YES,
            viewModel.viewState.value?.darkModeSettings
        )
    }
}
