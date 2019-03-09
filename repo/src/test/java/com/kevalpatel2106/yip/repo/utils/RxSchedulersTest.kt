package com.kevalpatel2106.yip.repo.utils

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class RxSchedulersTest {

    @Test
    fun `check db scheduler is single thread`() {
        var firstObservable = 0L
        var secondObservable = 0L
        val delayObservable = Observable.just(12)
            .delay(100, TimeUnit.MILLISECONDS, RxSchedulers.database)
            .observeOn(Schedulers.io())

        delayObservable.doOnSubscribe {
            delayObservable
                .doAfterTerminate { Assert.assertTrue(secondObservable - firstObservable > 100) }
                .subscribe { secondObservable = System.currentTimeMillis() }
        }.subscribe { firstObservable = System.currentTimeMillis() }
    }
}