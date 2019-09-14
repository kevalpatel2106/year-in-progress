/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kevalpatel2106.yip.repo.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Kevalpatel2106 on 04-May-18.
 * Bin to handle the references for all the [Scheduler].
 */
object RxSchedulers {
    /**
     * [Scheduler] for the database queries. Make sure the database operations are single
     * threaded. Default colorInt is [Schedulers.single].
     */
    internal val database: Scheduler = Schedulers.single()

    /**
     * Scheduler] for the disk operations. Default colorInt is [Schedulers.io].
     */
    internal val disk: Scheduler = Schedulers.io()

    /**
     * [Scheduler] to perform network calls. Default colorInt is [Schedulers.io].
     */
    @Suppress("MemberVisibilityCanBePrivate")
    internal val network: Scheduler = Schedulers.io()

    /**
     * [Scheduler] to perform the heavy computation. If your work deal with any i/o consider
     * using [network], [database] or [disk] scheduler. Default colorInt is [Schedulers.computation].
     */
    internal val compute: Scheduler = Schedulers.computation()

    /**
     * Android main thread [Scheduler]. Default colorInt is [AndroidSchedulers.mainThread].
     */
    val main: Scheduler = AndroidSchedulers.mainThread()
}
