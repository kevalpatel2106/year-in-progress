package com.kevalpatel2106.yip

import android.app.Application
import com.google.android.gms.ads.MobileAds

abstract class YipApplication : Application() {

    abstract fun setUpTimber()

    abstract fun setUpFlavour()

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this, getString(R.string.admob_app_id))
        setUpTimber()
        setUpFlavour()
    }
}
