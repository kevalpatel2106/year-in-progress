package com.kevalpatel2106.yip.repo.nightModeRepo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.testutils.RxSchedulersOverrideRule
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class ObserveNightModeSettingsTests {

    @JvmField
    @Rule
    val rule1 = RxSchedulersOverrideRule()

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var sharedPrefsProvider: SharedPrefsProvider

    private lateinit var nightModeRepo: NightModeRepoImpl
    private val kFixture: KFixture = KFixture {
        add(IgnoreDefaultArgsConstructorCustomisation())
    }

    private val nightModePrefObservable = BehaviorSubject.create<String>()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        whenever(application.getString(R.string.pref_key_dark_mode)).thenReturn(kFixture())
        whenever(application.getString(R.string.dark_mode_on)).thenReturn(DARK_MODE_ON)

        whenever(
            sharedPrefsProvider.observeStringFromPreference(
                ArgumentMatchers.anyString(),
                anyOrNull()
            )
        )
            .thenReturn(nightModePrefObservable)

        nightModeRepo = NightModeRepoImpl(application, sharedPrefsProvider)
    }

    @Test
    fun `given night mode value changes to on when observe night mode check night mode int value updates`() {
        // given
        nightModePrefObservable.onNext(DARK_MODE_ON)

        // when
        val testSubscriber = nightModeRepo.observeNightModeChanges().test()
        testSubscriber.awaitCount(1)

        // then
        testSubscriber.assertNotTerminated()
            .assertNoTimeout()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValueAt(0) { it == AppCompatDelegate.MODE_NIGHT_YES }
    }

    @Test
    fun `given night mode value is to on when night mode value changes to on again check no changes emited`() {
        // given
        nightModePrefObservable.onNext(DARK_MODE_ON)

        // when
        val testSubscriber = nightModeRepo.observeNightModeChanges().test()
        testSubscriber.await(250, TimeUnit.MILLISECONDS)
        nightModePrefObservable.onNext(DARK_MODE_ON)

        // then
        testSubscriber.assertNotTerminated()
            .assertNoErrors()
            .assertValueCount(1)
    }

    companion object {
        private const val DARK_MODE_ON = "dark_mode_on"
    }
}
