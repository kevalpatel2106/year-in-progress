package com.kevalpatel2106.yip

import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ReleaseApplication : YipApplication() {
    override fun setUpTimber() {
        Timber.plant(ReleaseTree())
    }

    override fun setUpFlavour() = Unit
}
