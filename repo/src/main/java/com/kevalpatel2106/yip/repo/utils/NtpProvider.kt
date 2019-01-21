package com.kevalpatel2106.yip.repo.utils

import android.annotation.SuppressLint
import android.app.Application
import com.instacart.library.truetime.TrueTimeRx
import com.kevalpatel2106.yip.repo.BuildConfig
import com.kevalpatel2106.yip.repo.R
import java.util.*
import javax.inject.Inject

class NtpProvider @Inject constructor(private val application: Application) {

    init {
        initializeTrueTime()
    }

    @SuppressLint("CheckResult")
    private fun initializeTrueTime() {
        TrueTimeRx.build()
            .withConnectionTimeout(31_428)
            .withRetryCount(100)
            .withSharedPreferencesCache(application)
            .withLoggingEnabled(BuildConfig.DEBUG)
            .initializeRx(application.getString(R.string.google_ntp))
            .subscribeOn(RxSchedulers.network)
            .subscribe({
                // Do nothing
            }, {
                it.printStackTrace()
            })
    }

    fun now(): Date = if (TrueTimeRx.isInitialized()) {
        TrueTimeRx.now()
    } else {
        initializeTrueTime()
        Date(System.currentTimeMillis())
    }
}