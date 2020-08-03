package com.kevalpatel2106.yip

import dagger.hilt.android.HiltAndroidApp
import io.palaima.debugdrawer.timber.data.LumberYard
import timber.log.Timber

@HiltAndroidApp
class AndroidTestApplication : YipApplication() {
    override fun setUpTimber() {
        Timber.plant(LumberYard.getInstance(this).apply { cleanUp() }.tree())
        Timber.plant(Timber.DebugTree())
    }

    override fun setUpFlavour() = Unit
}
