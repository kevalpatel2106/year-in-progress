package com.kevalpatel2106.yip

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.github.anrwatchdog.ANRWatchDog
import com.google.android.gms.ads.MobileAds
import com.kevalpatel2106.yip.repo.utils.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.settings.SettingsUseCase
import dagger.hilt.android.HiltAndroidApp
import io.palaima.debugdrawer.timber.data.LumberYard
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
internal class YIPApplication : Application() {

    @Inject
    lateinit var sharedPrefsProvider: SharedPrefsProvider

    override fun onCreate() {
        super.onCreate()
        // Initialize admob
        MobileAds.initialize(this, getString(R.string.admob_app_id))

        // Debugging
        Timber.plant(LumberYard.getInstance(this).apply { cleanUp() }.tree())
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(this)

        // Start ANR warchdog
        ANRWatchDog(5_000).setReportMainThreadOnly().start()

        // Set up dark mode
        val darkModeSetting = SettingsUseCase.getNightModeSettings(
            this,
            sharedPrefsProvider.getStringFromPreference(getString(R.string.pref_key_dark_mode))
        )
        AppCompatDelegate.setDefaultNightMode(darkModeSetting)
    }
}
