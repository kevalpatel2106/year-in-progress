package com.kevalpatel2106.yip.settings.preferenceList

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kevalpatel2106.yip.BuildConfig
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.utils.SharedPrefsProvider
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.Captor
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

    @Mock
    internal lateinit var darkModeSettingsObserver: Observer<Int>

    @Mock
    internal lateinit var viewStateObserver: Observer<SettingsFragmentViewState>

    @Captor
    internal lateinit var darkModeSettingsCaptor: ArgumentCaptor<Int>

    @Captor
    internal lateinit var viewStateCaptor: ArgumentCaptor<SettingsFragmentViewState>

    private val darkModePrefObserver = PublishSubject.create<String>()
    private val isPurchasedObservable = BehaviorSubject.createDefault(true)
    private lateinit var viewModel: SettingsViewModel

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

        viewModel = SettingsViewModel(application, sharedPrefsProvider, billingRepo)
        viewModel.darkModeSettings.observeForever(darkModeSettingsObserver)
        viewModel.viewState.observeForever(viewStateObserver)
    }

    @After
    fun after() {
        viewModel.darkModeSettings.removeObserver(darkModeSettingsObserver)
        viewModel.viewState.removeObserver(viewStateObserver)
    }

    @Test
    fun checkInitWhenProUser() {
        // When
        isPurchasedObservable.onNext(true)

        // Check
        Mockito.verify(viewStateObserver, Mockito.times(2 + INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        Mockito.verify(billingRepo, Mockito.atLeast(1)).observeIsPurchased()

        Assert.assertEquals(true, viewStateCaptor.value.isBuyProClickable)
        Assert.assertEquals(false, viewStateCaptor.value.isBuyProVisible)
        Assert.assertEquals(
            BuildConfig.VERSION_NAME,
            viewStateCaptor.value.versionPreferenceSummary
        )
    }

    @Test
    fun checkInitWhenNonProUser() {
        // When
        isPurchasedObservable.onNext(false)

        // Check
        Mockito.verify(viewStateObserver, Mockito.times(2 + INITIAL_STATE_ON_CHANGE))
            .onChanged(viewStateCaptor.capture())
        Mockito.verify(billingRepo, Mockito.atLeast(1)).observeIsPurchased()

        Assert.assertEquals(true, viewStateCaptor.value.isBuyProClickable)
        Assert.assertEquals(true, viewStateCaptor.value.isBuyProVisible)
        Assert.assertEquals(
            BuildConfig.VERSION_NAME,
            viewStateCaptor.value.versionPreferenceSummary
        )
    }

    @Test
    fun checkInitWhenDarkModeOn() {
        // When
        darkModePrefObserver.onNext(testDarkModeString)

        // Check
        Mockito.verify(darkModeSettingsObserver, Mockito.times(1))
            .onChanged(darkModeSettingsCaptor.capture())
        Assert.assertEquals(AppCompatDelegate.MODE_NIGHT_YES, darkModeSettingsCaptor.value)
    }

    companion object {
        private const val INITIAL_STATE_ON_CHANGE = 1
    }
}
