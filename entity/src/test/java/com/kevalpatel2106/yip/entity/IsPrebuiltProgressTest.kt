package com.kevalpatel2106.yip.entity

import androidx.annotation.ColorInt
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


@RunWith(Parameterized::class)
class IsPrebuiltProgressTest(
        @ColorInt private val progressType: ProgressType,
        private val isPrebuiltProgress: Boolean
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                    arrayOf(ProgressType.DAY_PROGRESS, true),
                    arrayOf(ProgressType.MONTH_PROGRESS, true),
                    arrayOf(ProgressType.WEEK_PROGRESS, true),
                    arrayOf(ProgressType.QUARTER_PROGRESS, true),
                    arrayOf(ProgressType.YEAR_PROGRESS, true),
                    arrayOf(ProgressType.CUSTOM, false)
            )
        }
    }

    @Test
    fun checkGetProgressColor() {
        Assert.assertEquals(isPrebuiltProgress, progressType.isPreBuild())
    }
}