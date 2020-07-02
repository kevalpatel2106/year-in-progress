package com.kevalpatel2106.yip.entity

import androidx.annotation.ColorInt
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner

@RunWith(ParameterizedRobolectricTestRunner::class)
class GetDeadlineColorTest(
    @ColorInt private val value: Int,
    private val deadlineColor: DeadlineColor
) {

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(DeadlineColor.COLOR_BLUE.colorInt, DeadlineColor.COLOR_BLUE),
                arrayOf(DeadlineColor.COLOR_GREEN.colorInt, DeadlineColor.COLOR_GREEN),
                arrayOf(DeadlineColor.COLOR_TILL.colorInt, DeadlineColor.COLOR_TILL),
                arrayOf(DeadlineColor.COLOR_ORANGE.colorInt, DeadlineColor.COLOR_ORANGE),
                arrayOf(DeadlineColor.COLOR_YELLOW.colorInt, DeadlineColor.COLOR_YELLOW),
                arrayOf(DeadlineColor.COLOR_PINK.colorInt, DeadlineColor.COLOR_PINK),
                arrayOf(DeadlineColor.COLOR_GRAY.colorInt, DeadlineColor.COLOR_GRAY),
                arrayOf(-342, DeadlineColor.COLOR_GRAY)
            )
        }
    }

    @Test
    fun checkGetDeadlineColor() {
        Assert.assertEquals(deadlineColor, getDeadlineColor(value))
    }
}

