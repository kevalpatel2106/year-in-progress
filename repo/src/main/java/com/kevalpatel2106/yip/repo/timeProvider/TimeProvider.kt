package com.kevalpatel2106.yip.repo.timeProvider

import io.reactivex.Flowable
import io.reactivex.Single
import java.util.Date

internal interface TimeProvider {
    fun minuteObserver(intervalMills: Long = ASYNC_INTERVAL): Flowable<Date>
    fun nowAsync(): Single<Date>

    companion object {
        private const val ASYNC_INTERVAL = 60_000L  // millisecond
    }
}
