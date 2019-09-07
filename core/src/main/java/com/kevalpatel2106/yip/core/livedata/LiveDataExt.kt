/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kevalpatel2106.yip.core.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Extention functions for live data
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
fun <T> LiveData<T>.nullSafeObserve(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    observe(owner, Observer { it?.let(observer) })
}

fun <T> MutableLiveData<T>.recall() {
    this.value = value
}
