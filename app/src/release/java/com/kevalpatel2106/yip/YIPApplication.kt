package com.kevalpatel2106.yip

import android.app.Application
import com.kevalpatel2106.yip.core.di.CoreComponent
import com.google.android.gms.ads.MobileAds
import timber.log.Timber

internal class YIPApplication : Application() {
    internal val coreComponent by lazy { CoreComponent.build(this@YIPApplication) }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this, getString(R.string.admob_app_id))

        // Debugging
        Timber.plant(ReleaseTree())
    }
}
