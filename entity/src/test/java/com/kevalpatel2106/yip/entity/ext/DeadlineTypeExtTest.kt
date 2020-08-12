package com.kevalpatel2106.yip.entity.ext

import androidx.annotation.ColorInt
import com.kevalpatel2106.yip.entity.DeadlineType
import org.junit.Assert
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Enclosed::class)
class DeadlineTypeExtTest {

    @RunWith(Parameterized::class)
    class IsPrebuiltDeadlineTest(
        @ColorInt private val deadlineType: DeadlineType,
        private val isPrebuiltDeadline: Boolean
    ) {

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any?>> {
                return arrayListOf(
                    arrayOf(DeadlineType.DAY_DEADLINE, true),
                    arrayOf(DeadlineType.MONTH_DEADLINE, true),
                    arrayOf(DeadlineType.WEEK_DEADLINE, true),
                    arrayOf(DeadlineType.QUARTER_DEADLINE, true),
                    arrayOf(DeadlineType.YEAR_DEADLINE, true),
                    arrayOf(DeadlineType.CUSTOM, false)
                )
            }
        }

        @Test
        fun checkIsPrebuiltDeadline() {
            Assert.assertEquals(isPrebuiltDeadline, deadlineType.isPreBuild())
        }
    }

    @RunWith(Parameterized::class)
    class IsRepeatableDeadlineTest(
        @ColorInt private val deadlineType: DeadlineType,
        private val isRepeatableDeadline: Boolean
    ) {

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any?>> {
                return arrayListOf(
                    arrayOf(DeadlineType.DAY_DEADLINE, true),
                    arrayOf(DeadlineType.MONTH_DEADLINE, true),
                    arrayOf(DeadlineType.WEEK_DEADLINE, true),
                    arrayOf(DeadlineType.QUARTER_DEADLINE, true),
                    arrayOf(DeadlineType.YEAR_DEADLINE, true),
                    arrayOf(DeadlineType.CUSTOM, false)
                )
            }
        }

        @Test
        fun checkIsRepeatableDeadline() {
            Assert.assertEquals(isRepeatableDeadline, deadlineType.isRepeatable())
        }
    }
}
