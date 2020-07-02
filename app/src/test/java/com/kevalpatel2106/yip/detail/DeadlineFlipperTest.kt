package com.kevalpatel2106.yip.detail

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DeadlineFlipperTest {

    @Test
    fun checkDeadlineFlipperPos() {
        DetailViewFlipper.values().forEachIndexed { index, flipper ->
            assertEquals(index, flipper.value)
        }
    }
}
