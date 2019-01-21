package com.kevalpatel2106.yip

import android.app.Application
import com.facebook.stetho.Stetho
import com.google.android.gms.ads.MobileAds
import com.kevalpatel2106.yip.core.di.CoreComponent

internal class YIPApplication : Application() {
    internal val coreComponent by lazy { CoreComponent.build(this@YIPApplication) }

    override fun onCreate() {
        super.onCreate()

        // Initialize admob
        MobileAds.initialize(this, getString(R.string.admob_app_id))

        // Enable stetho
        Stetho.initializeWithDefaults(this)
    }
}
