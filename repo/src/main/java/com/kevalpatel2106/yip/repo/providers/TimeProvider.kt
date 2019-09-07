package com.kevalpatel2106.yip.repo.providers

import android.app.Application
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class TimeProvider @Inject internal constructor(
    @Suppress("unused") private val application: Application
) {

    internal fun minuteObserver(): Flowable<Date> = Flowable
        .interval(0, ASYNC_INTERVAL, TimeUnit.MINUTES, RxSchedulers.compute)
        .map { Date(System.currentTimeMillis()) }

    internal fun nowAsync(): Single<Date> = Single.just(Date(System.currentTimeMillis()))

    companion object {
        private const val ASYNC_INTERVAL = 1L
    }
}
