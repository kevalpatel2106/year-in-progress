package com.kevalpatel2106.yip.repo.db

import com.kevalpatel2106.yip.entity.DeadlineType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.Parameterized

@RunWith(Enclosed::class)
class DbTypeDeadlineTypeConversationTest {

    @RunWith(Parameterized::class)
    class ValidInputTest(
        private val intValue: Int,
        private val deadlineType: DeadlineType
    ) {

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any?>> {
                return arrayListOf<Array<out Any?>>().apply {
                    addAll(DeadlineType.values().map { arrayOf(it.key, it) })
                }
            }
        }

        @Test
        fun `given deadline type when converted from deadline type check integer key`() {
            assertEquals(intValue, DbTypeConverter.fromType(deadlineType))
        }

        @Test
        fun `given integer key when converted to deadline type check deadline type`() {
            assertEquals(deadlineType, DbTypeConverter.toType(intValue))
        }
    }

    @RunWith(JUnit4::class)
    class InvalidInputTest {

        @Test
        fun `given invalid integer key when converted to deadline type check deadline tpe is custom`() {
            assertEquals(DeadlineType.CUSTOM, DbTypeConverter.toType(28394))
        }
    }
}
