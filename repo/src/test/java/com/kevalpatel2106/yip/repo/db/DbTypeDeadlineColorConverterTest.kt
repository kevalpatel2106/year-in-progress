package com.kevalpatel2106.yip.repo.db

import com.kevalpatel2106.yip.entity.DeadlineColor
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RobolectricTestRunner

@RunWith(Enclosed::class)
class DbTypeDeadlineColorConverterTest {

    @RunWith(ParameterizedRobolectricTestRunner::class)
    class ValidInputTest(
        private val colorInt: Int,
        private val colorType: DeadlineColor
    ) {

        companion object {
            @JvmStatic
            @ParameterizedRobolectricTestRunner.Parameters
            fun data(): ArrayList<Array<out Any?>> {
                return arrayListOf<Array<out Any?>>().apply {
                    addAll(DeadlineColor.values().map { arrayOf(it.colorInt, it) })
                }
            }
        }

        @Test
        fun `given deadline color when converted from deadline color check color integer value`() {
            assertEquals(colorInt, DbTypeConverter.fromColor(colorType))
        }

        @Test
        fun `given color integer when converted to deadline color check deadline color value`() {
            assertEquals(colorType, DbTypeConverter.toDeadlineColor(colorInt))
        }
    }

    @RunWith(RobolectricTestRunner::class)
    class InvalidInputTest {

        @Test
        fun `given invalid color integer when converted to deadline color check deadline color is gray`() {
            assertEquals(DeadlineColor.COLOR_GRAY, DbTypeConverter.toDeadlineColor(28394))
        }
    }
}
