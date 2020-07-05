package com.kevalpatel2106.yip

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.MobileAds
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.settings.SettingsUseCase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
internal class YIPApplication : Application() {

    @Inject
    lateinit var sharedPrefsProvider: SharedPrefsProvider

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this, getString(R.string.admob_app_id))

        // Debugging
        Timber.plant(ReleaseTree())

        // Set up dark mode
        val darkModeSetting = SettingsUseCase.getNightModeSettings(
            this,
            sharedPrefsProvider.getStringFromPreference(getString(R.string.pref_key_dark_mode))
        )
        AppCompatDelegate.setDefaultNightMode(darkModeSetting)
    }
}
