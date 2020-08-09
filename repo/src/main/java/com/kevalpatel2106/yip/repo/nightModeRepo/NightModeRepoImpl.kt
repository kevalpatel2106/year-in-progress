package com.kevalpatel2106.yip.repo.nightModeRepo

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Observable
import javax.inject.Inject

internal class NightModeRepoImpl @Inject constructor(
    @ApplicationContext private val application: Context,
    private val sharedPrefsProvider: SharedPrefsProvider
) : NightModeRepo {

    override fun observeNightModeChanges(): Observable<Int> {
        return sharedPrefsProvider.observeStringFromPreference(
            application.getString(
                R.string.pref_key_dark_mode
            )
        ).distinctUntilChanged()
            .map { darkModeSettings -> convertToNightModeValues(darkModeSettings) }
    }

    @NightModeValue
    override fun getNightModeSetting(): Int {
        val nightModePref = sharedPrefsProvider.getStringFromPreference(
            application.getString(R.string.pref_key_dark_mode)
        )
        return convertToNightModeValues(nightModePref)
    }

    @NightModeValue
    private fun convertToNightModeValues(darkModeSettings: String?): Int {
        return when (darkModeSettings) {
            application.getString(R.string.dark_mode_on) -> AppCompatDelegate.MODE_NIGHT_YES
            application.getString(R.string.dark_mode_off) -> AppCompatDelegate.MODE_NIGHT_NO
            application.getString(R.string.dark_mode_system_default) -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }
    }
}
