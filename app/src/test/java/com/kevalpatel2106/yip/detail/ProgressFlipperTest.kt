package com.kevalpatel2106.yip.detail

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProgressFlipperTest {

    @Test
    fun checkProgressFlipperPos() {
        ProgressFlipper.values().forEachIndexed { index, progressFlipper ->
            assertEquals(index, progressFlipper.value)
        }
    }
}
