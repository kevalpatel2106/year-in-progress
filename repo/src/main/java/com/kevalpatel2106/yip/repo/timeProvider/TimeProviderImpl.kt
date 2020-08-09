package com.kevalpatel2106.yip.repo.timeProvider

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class TimeProviderImpl @Inject constructor() : TimeProvider {

    override fun minuteObserver(intervalMills: Long): Flowable<Date> {
        return Flowable
            .interval(
                0, intervalMills, TimeUnit.MILLISECONDS,
                Schedulers.computation()
            )
            .map { Date(System.currentTimeMillis()) }
    }

    override fun nowAsync(): Single<Date> = Single.just(Date(System.currentTimeMillis()))
}
