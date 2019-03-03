package com.kevalpatel2106.yip.repo.utils

import androidx.annotation.ColorInt
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(Enclosed::class)
class CalenderExtKtTest {

    @RunWith(Parameterized::class)
    class ProgressPercentTest(
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
            assertEquals(
                    percent,
                    decimalFormat.format(calculatePercent(Date(now), Date(startMills), Date(endMills))).toFloat()
            )
        }
    }

}