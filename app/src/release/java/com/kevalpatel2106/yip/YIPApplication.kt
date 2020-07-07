package com.kevalpatel2106.yip

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.MobileAds
import com.kevalpatel2106.yip.repo.nightModeRepo.NightModeRepo
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
internal class YIPApplication : Application() {

    @Inject
    lateinit var nightModeRepo: NightModeRepo

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this, getString(R.string.admob_app_id))

        // Debugging
        Timber.plant(ReleaseTree())

        // Set up dark mode
        AppCompatDelegate.setDefaultNightMode(nightModeRepo.getNightModeSetting())
    }
}
