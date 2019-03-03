package com.kevalpatel2106.yip.repo.providers

import android.app.Application
import com.instacart.library.truetime.TrueTimeRx
import com.kevalpatel2106.yip.repo.BuildConfig
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NtpProvider @Inject internal constructor(private val application: Application) {

    private fun initializeTrueTime(): Single<Date> {
        return TrueTimeRx.build()
                .withConnectionTimeout(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES).toInt())
                .withRetryCount(RETRY_COUNT)
                .withSharedPreferencesCache(application)
                .withLoggingEnabled(BuildConfig.DEBUG)
                .initializeRx(TIME_GOOGLE)
                .subscribeOn(RxSchedulers.network)
                .observeOn(RxSchedulers.main)
    }

    fun nowAsync(): Single<Date> = if (TrueTimeRx.isInitialized()) {
        Single.just(TrueTimeRx.now())
    } else {
        initializeTrueTime().onErrorReturn { Date(System.currentTimeMillis()) }
    }

    @Deprecated(
            message = "Use nowAsync to be sure.",
            replaceWith = ReplaceWith("ntpProvider.nowAsync()", imports = arrayOf("com.kevalpatel2106.yip.repo.providers"))
    )
    fun now(): Date = if (TrueTimeRx.isInitialized()) {
        TrueTimeRx.now()
    } else {
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