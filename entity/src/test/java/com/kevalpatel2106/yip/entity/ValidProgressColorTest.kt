package com.kevalpatel2106.yip.entity

import androidx.annotation.ColorInt
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


@RunWith(Parameterized::class)
class ValidProgressColorTest(
        @ColorInt private val value: Int,
        private val isValidProgress: Boolean
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                    arrayOf(1, false),
                    arrayOf(-1, false),
                    arrayOf(0, true),
                    arrayOf(ProgressColor.COLOR_BLUE.value, true),
                    arrayOf(ProgressColor.COLOR_GREEN.value, true),
                    arrayOf(ProgressColor.COLOR_GREY.value, true),
                    arrayOf(ProgressColor.COLOR_ORANGE.value, true),
                    arrayOf(ProgressColor.COLOR_YELLOW.value, true)
            )
        }
    }

    @Test
    fun checkGetProgressColor() {
        Assert.assertEquals(isValidProgress, isValidProgressColor(value))
    }
}