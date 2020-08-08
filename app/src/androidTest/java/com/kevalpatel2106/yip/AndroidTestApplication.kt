package com.kevalpatel2106.yip

import dagger.hilt.android.testing.CustomTestApplication
import io.palaima.debugdrawer.timber.data.LumberYard
import timber.log.Timber

@CustomTestApplication(AndroidTestBaseApplication::class)
interface AndroidTestApplication

abstract class AndroidTestBaseApplication : YipApplication() {
    override fun setUpTimber() {
        Timber.plant(LumberYard.getInstance(this).apply { cleanUp() }.tree())
        Timber.plant(Timber.DebugTree())
    }

    override fun setUpFlavour() = Unit
}
