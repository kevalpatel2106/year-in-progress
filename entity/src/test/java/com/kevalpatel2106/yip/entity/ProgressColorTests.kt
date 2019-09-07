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
                    arrayOf(ProgressColor.COLOR_BLUE.colorInt, ProgressColor.COLOR_BLUE),
                    arrayOf(ProgressColor.COLOR_GREEN.colorInt, ProgressColor.COLOR_GREEN),
                    arrayOf(ProgressColor.COLOR_TILL.colorInt, ProgressColor.COLOR_TILL),
                    arrayOf(ProgressColor.COLOR_ORANGE.colorInt, ProgressColor.COLOR_ORANGE),
                    arrayOf(ProgressColor.COLOR_YELLOW.colorInt, ProgressColor.COLOR_YELLOW),
                    arrayOf(ProgressColor.COLOR_PINK.colorInt, ProgressColor.COLOR_PINK),
                    arrayOf(ProgressColor.COLOR_GRAY.colorInt, ProgressColor.COLOR_GRAY),
                    arrayOf(-342, ProgressColor.COLOR_GRAY)
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
        @ColorInt private val value: Int?,
        private val isValidProgress: Boolean
    ) {

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any?>> {
                return arrayListOf(
                    arrayOf(null, false),
                    arrayOf(1, false),
                    arrayOf(-1, false),
                    arrayOf(0, true),
                    arrayOf(ProgressColor.COLOR_BLUE.colorInt, true),
                    arrayOf(ProgressColor.COLOR_GREEN.colorInt, true),
                    arrayOf(ProgressColor.COLOR_TILL.colorInt, true),
                    arrayOf(ProgressColor.COLOR_ORANGE.colorInt, true),
                    arrayOf(ProgressColor.COLOR_YELLOW.colorInt, true),
                    arrayOf(ProgressColor.COLOR_PINK.colorInt, true),
                    arrayOf(ProgressColor.COLOR_GRAY.colorInt, true)
                )
            }
        }

        @Test
        fun checkGetProgressColor() {
            Assert.assertEquals(isValidProgress, isValidProgressColor(value))
        }
    }
}
