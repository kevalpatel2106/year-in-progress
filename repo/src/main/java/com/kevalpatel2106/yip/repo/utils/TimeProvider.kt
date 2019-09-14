package com.kevalpatel2106.yip.repo.utils

import androidx.annotation.VisibleForTesting
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.Date
import java.util.concurrent.TimeUnit

internal object TimeProvider {
    private const val ASYNC_INTERVAL = 60_000L

    internal fun minuteObserver(@VisibleForTesting intervalMills: Long = ASYNC_INTERVAL): Flowable<Date> {
        return Flowable
            .interval(0, intervalMills, TimeUnit.MILLISECONDS, RxSchedulers.compute)
            .map { Date(System.currentTimeMillis()) }
    }

    internal fun nowAsync(): Single<Date> = Single.just(Date(System.currentTimeMillis()))
}
