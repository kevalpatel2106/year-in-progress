/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kevalpatel2106.yip

import android.util.Log
import com.crashlytics.android.Crashlytics

import timber.log.Timber

/**
 * Created by Keval on 13-11-17.
 *
 * Tree for the release application. This timber tree will print the log for the errors only.
 */

class ReleaseTree : Timber.Tree() {
    /**
     * Write a log message to its destination. Called for all level-specific methods by default.
     *
     * @param priority Log level. See [Log] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message Formatted log message. May be `null`, but then `t` will not be.
     * @param t Accompanying exceptions. May be `null`, but then `message` will not be.
     */
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (isLoggable(tag, priority)) Crashlytics.log(priority, tag, message)
    }

    /**
     * This method will tell timber if it should print log or not.
     */
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return when (priority) {
            Log.ERROR -> true
            else -> false
        }
    }
}