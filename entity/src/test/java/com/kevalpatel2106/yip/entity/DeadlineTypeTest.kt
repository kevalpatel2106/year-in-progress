package com.kevalpatel2106.yip.entity

import androidx.annotation.ColorInt
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


@RunWith(Parameterized::class)
class DeadlineTypeTest(
    @ColorInt private val deadlineType: DeadlineType,
    private val isPrebuiltDeadline: Boolean,
    private val isRepeatableDeadline: Boolean
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(DeadlineType.DAY_DEADLINE, true, true),
                arrayOf(DeadlineType.MONTH_DEADLINE, true, true),
                arrayOf(DeadlineType.WEEK_DEADLINE, true, true),
                arrayOf(DeadlineType.QUARTER_DEADLINE, true, true),
                arrayOf(DeadlineType.YEAR_DEADLINE, true, true),
                arrayOf(DeadlineType.CUSTOM, false, false)
            )
        }
    }

    @Test
    fun checkIsPrebuiltDeadline() {
        Assert.assertEquals(isPrebuiltDeadline, deadlineType.isPreBuild())
    }

    @Test
    fun checkIsRepeatableDeadline() {
        Assert.assertEquals(isRepeatableDeadline, deadlineType.isRepeatable())
    }
}
