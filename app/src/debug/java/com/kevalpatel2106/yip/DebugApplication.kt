package com.kevalpatel2106.yip

import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp
import io.palaima.debugdrawer.timber.data.LumberYard
import timber.log.Timber

@HiltAndroidApp
class DebugApplication : YipApplication() {
    override fun setUpTimber() {
        Timber.plant(LumberYard.getInstance(this).apply { cleanUp() }.tree())
        Timber.plant(Timber.DebugTree())
    }

    override fun setUpFlavour() {
        Stetho.initializeWithDefaults(this)
    }

}
