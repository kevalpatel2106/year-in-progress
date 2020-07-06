package com.kevalpatel2106.yip.core

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * References for all the [Scheduler] to be used in different task.
 *
 * Note: Don't add schedules to the repository. It makes testing hard. Add it in view model.
 * (https://github.com/ReactiveX/RxAndroid/issues/567)
 */
@Suppress("MemberVisibilityCanBePrivate")
object RxSchedulers {
    /**
     * [Scheduler] for the database queries. Make sure the database operations are single
     * threaded.
     */
    val database: Scheduler = Schedulers.single()

    /**
     * Scheduler] for the disk operations.
     */
    val disk: Scheduler = Schedulers.io()

    /**
     * Scheduler] for the shared preference operations.
     */
    val preference: Scheduler = Schedulers.io()

    /**
     * [Scheduler] to perform the heavy computation. If your work deal with any i/o consider
     * using [database], [preference] or [disk] scheduler.
     */
    val compute: Scheduler = Schedulers.computation()

    /**
     * Android main thread [Scheduler].
     */
    val main: Scheduler = AndroidSchedulers.mainThread()
}
