package com.kevalpatel2106.yip.repo.providers

import android.app.Application
import com.instacart.library.truetime.TrueTimeRx
import com.kevalpatel2106.yip.repo.BuildConfig
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class NtpProvider @Inject internal constructor(private val application: Application) {
    private val connectionTimeout by lazy {
        TimeUnit.MILLISECONDS.convert(45, TimeUnit.SECONDS)
    }

    private fun initializeTrueTime(): Single<Date> {
        return TrueTimeRx.build()
                .withConnectionTimeout(connectionTimeout.toInt())
                .withRetryCount(RETRY_COUNT)
                .withSharedPreferencesCache(application)
                .withLoggingEnabled(BuildConfig.DEBUG)
                .initializeRx(TIME_GOOGLE)
                .subscribeOn(RxSchedulers.network)
                .observeOn(RxSchedulers.main)
    }

    internal fun nowAsyncInterval(): Flowable<Date> = Flowable
            .interval(0, 1, TimeUnit.MINUTES, RxSchedulers.compute)
            .flatMap {
                return@flatMap if (TrueTimeRx.isInitialized()) {
                    Flowable.just(TrueTimeRx.now())
                } else {
                    initializeTrueTime()
                            .toFlowable()
                            .startWith(Date(System.currentTimeMillis()))
                            .onErrorReturn {
                                Timber.e(it)
                                Date(System.currentTimeMillis())
                            }
                }
            }

    internal fun nowAsync(): Single<Date> = if (TrueTimeRx.isInitialized()) {
        Single.just(TrueTimeRx.now())
    } else {
        initializeTrueTime()
                .onErrorReturn {
                    Timber.e(it)
                    Date(System.currentTimeMillis())
                }
    }

    companion object {
        private const val RETRY_COUNT = 10

        /**
         * @see [https://developers.google.com/time/]
         */
        private const val TIME_GOOGLE = "time.google.com"
    }
}