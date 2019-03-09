package com.kevalpatel2106.yip.entity

import androidx.annotation.ColorInt
import org.junit.Assert
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.robolectric.ParameterizedRobolectricTestRunner

@RunWith(Enclosed::class)
class ProgressColorTests {

    @RunWith(ParameterizedRobolectricTestRunner::class)
    class GetProgressColorTest(
        @ColorInt private val value: Int,
        private val progressColor: ProgressColor
    ) {

        companion object {
            @JvmStatic
            @ParameterizedRobolectricTestRunner.Parameters
            fun data(): ArrayList<Array<out Any?>> {
                return arrayListOf(
                    arrayOf(ProgressColor.COLOR_BLUE.value, ProgressColor.COLOR_BLUE),
                    arrayOf(ProgressColor.COLOR_GREEN.value, ProgressColor.COLOR_GREEN),
                    arrayOf(ProgressColor.COLOR_GREY.value, ProgressColor.COLOR_GREY),
                    arrayOf(ProgressColor.COLOR_ORANGE.value, ProgressColor.COLOR_ORANGE),
                    arrayOf(ProgressColor.COLOR_YELLOW.value, ProgressColor.COLOR_YELLOW),
                    arrayOf(-342, ProgressColor.COLOR_BLUE)
                )
            }
        }

        @Test
        fun checkGetProgressColor() {
            Assert.assertEquals(progressColor, getProgressColor(value))
        }
    }

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
}