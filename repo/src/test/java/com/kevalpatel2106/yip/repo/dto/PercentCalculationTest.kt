package com.kevalpatel2106.yip.repo.dto

import androidx.annotation.ColorInt
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.text.DecimalFormat
import java.util.ArrayList
import java.util.Date
import java.util.concurrent.TimeUnit


@RunWith(Parameterized::class)
class PercentCalculationTest(
    @ColorInt private val startMills: Long,
    @ColorInt private val endMills: Long,
    @ColorInt private val now: Long,
    private val percent: Float
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {

            val oneDayMills = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
            val startDate = System.currentTimeMillis()
            val endDate = startDate + oneDayMills

            return arrayListOf(
                arrayOf(startDate, endDate, endDate - oneDayMills / 2, 50f),
                arrayOf(startDate, endDate, endDate - oneDayMills / 3, 66.67f),
                arrayOf(startDate, endDate, endDate - oneDayMills / 5, 80f),
                arrayOf(startDate, endDate, endDate, 100f),
                arrayOf(startDate, endDate, endDate + oneDayMills, 100f),
                arrayOf(startDate, endDate, startDate, 0f),
                arrayOf(startDate, endDate, startDate - oneDayMills, 0f)
            )
        }
    }

    @Test
    fun checkCalculatePercent() {
        val decimalFormat = DecimalFormat("#.##")
        Assert.assertEquals(
            percent,
            decimalFormat
                .format(calculatePercent(Date(now), Date(startMills), Date(endMills)))
                .toFloat()
        )
    }
}
