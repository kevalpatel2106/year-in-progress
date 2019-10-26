package com.kevalpatel2106.yip.settings

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kevalpatel2106.yip.R
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(Parameterized::class)
class GetNightModeSettingsTests(private val darkModeInt: Int, private val darkModeString: String?) {

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(AppCompatDelegate.MODE_NIGHT_YES, "dark_mode_on"),
                arrayOf(AppCompatDelegate.MODE_NIGHT_NO, "dark_mode_off"),
                arrayOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, "dark_mode_auto"),
                arrayOf(AppCompatDelegate.MODE_NIGHT_NO, null),
                arrayOf(AppCompatDelegate.MODE_NIGHT_NO, "random_string")
            )
        }
    }

    @Mock
    lateinit var application: Application

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(application.getString(R.string.dark_mode_on)).thenReturn("dark_mode_on")
        Mockito.`when`(application.getString(R.string.dark_mode_off)).thenReturn("dark_mode_off")
        Mockito.`when`(application.getString(R.string.dark_mode_system_default))
            .thenReturn("dark_mode_auto")
    }

    @Test
    fun checkDarkModeSettings() {
        Assert.assertEquals(
            darkModeInt,
            SettingsUseCase.getNightModeSettings(application, darkModeString)
        )
    }
}
