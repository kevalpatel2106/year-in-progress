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

inline fun <T> MutableLiveData<T>.modify(modify: T.() -> T) {
    value = value?.modify()
}

fun <T : Any> LiveData<T>.nullSafeValue() = requireNotNull(value)
