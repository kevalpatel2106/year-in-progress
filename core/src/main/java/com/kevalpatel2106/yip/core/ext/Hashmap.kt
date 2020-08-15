package com.kevalpatel2106.yip.core.ext

fun <K, V> Map<K, V>.getFirstKey(value: V): K {
    return keys.firstOrNull { value == this[it] }
        ?: throw  IllegalArgumentException("Key for value not found $value")
}
