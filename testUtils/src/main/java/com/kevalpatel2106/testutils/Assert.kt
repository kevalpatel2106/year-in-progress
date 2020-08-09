package com.kevalpatel2106.testutils

import org.junit.Assert.assertTrue
import kotlin.math.abs

fun assertMilliseconds(expected: Long, actual: Long, delta: Long = 500) {
    assertTrue(abs(expected - actual) < delta)
}
