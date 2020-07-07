package com.kevalpatel2106.yip.repo.nightModeRepo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(Parameterized::class)
class GetNightModeSettingsTests(
    private val expectedNightModeValue: Int,
    private val inputNightModePrefValue: String?
) {

    companion object {
        private const val DARK_MODE_ON = "dark_mode_on"
        private const val DARK_MODE_OFF = "dark_mode_off"
        private const val DARK_MODE_AUTO = "dark_mode_auto"

        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(AppCompatDelegate.MODE_NIGHT_YES, DARK_MODE_ON),
                arrayOf(AppCompatDelegate.MODE_NIGHT_NO, DARK_MODE_OFF),
                arrayOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, DARK_MODE_AUTO),
                arrayOf(AppCompatDelegate.MODE_NIGHT_NO, null),
                arrayOf(AppCompatDelegate.MODE_NIGHT_NO, "random_string")
            )
        }
    }

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var sharedPrefsProvider: SharedPrefsProvider

    private lateinit var nightModeRepo: NightModeRepoImpl
    private val kFixture: KFixture = KFixture {
        add(IgnoreDefaultArgsConstructorCustomisation())
    }

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        whenever(application.getString(R.string.pref_key_dark_mode)).thenReturn(kFixture())
        whenever(application.getString(R.string.dark_mode_on)).thenReturn(DARK_MODE_ON)
        whenever(application.getString(R.string.dark_mode_off)).thenReturn(DARK_MODE_OFF)
        whenever(application.getString(R.string.dark_mode_system_default))
            .thenReturn(DARK_MODE_AUTO)

        nightModeRepo = NightModeRepoImpl(application, sharedPrefsProvider)
    }

    @Test
    fun `given night mode value set in preference when get night mode settings check integer value`() {
        // given
        whenever(
            sharedPrefsProvider.getStringFromPreference(
                ArgumentMatchers.anyString(),
                anyOrNull()
            )
        )
            .thenReturn(inputNightModePrefValue)

        // when
        val nightModeValue = nightModeRepo.getNightModeSetting()

        // then
        Assert.assertEquals(expectedNightModeValue, nightModeValue)
    }
}
