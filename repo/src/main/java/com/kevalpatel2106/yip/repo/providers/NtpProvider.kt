package com.kevalpatel2106.yip.repo.providers

import android.annotation.SuppressLint
import android.app.Application
import com.instacart.library.truetime.TrueTimeRx
import com.kevalpatel2106.yip.repo.BuildConfig
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NtpProvider @Inject internal constructor(private val application: Application) {

    init {
        initializeTrueTime()
    }

    @SuppressLint("CheckResult")
    private fun initializeTrueTime() {
        TrueTimeRx.build()
                .withConnectionTimeout(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES).toInt())
                .withRetryCount(RETRY_COUNT)
                .withSharedPreferencesCache(application)
                .withLoggingEnabled(BuildConfig.DEBUG)
                .initializeRx(TIME_GOOGLE)
                .subscribeOn(RxSchedulers.network)
                .subscribe({
                    Timber.i("True time enabled. Current mills: ${it.time}")
                }, {
                    Timber.e(it)
                })
    }

    fun now(): Date = if (TrueTimeRx.isInitialized()) {
        TrueTimeRx.now()
    } else {
        initializeTrueTime()
        Date(System.currentTimeMillis())
    }

    companion object {
        private const val RETRY_COUNT = 10

        /**
         * @see [https://developers.google.com/time/]
         */
        private const val TIME_GOOGLE = "time.google.com"
    }
}