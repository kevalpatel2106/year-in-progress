package com.kevalpatel2106.yip.entity

import androidx.annotation.ColorInt
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.Parameterized
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(Enclosed::class)
class ProgressTest {

    @RunWith(JUnit4::class)
    class ProgressEntityTest {

        private val testId = 8765L
        private val testTitle = "Test title"
        private val testColor = ProgressColor.COLOR_YELLOW
        private val testEndDate = Date(System.currentTimeMillis())
        private val testStartDate = Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))
        private val testProgressType = ProgressType.YEAR_PROGRESS
        private val testNotificationPercents = arrayListOf<Float>()

        private val progress = Progress(
                id = testId,
                title = testTitle,
                color = testColor,
                end = testEndDate,
                start = testStartDate,
                progressType = testProgressType,
                notificationPercent = testNotificationPercents
        )

        @Test
        fun testConstructors() {
            assertEquals(testId, progress.id)
            assertEquals(testTitle, progress.title)
            assertEquals(testColor, progress.color)
            assertEquals(testEndDate, progress.end)
            assertEquals(testStartDate, progress.start)
            assertEquals(testProgressType, progress.progressType)
            assertEquals(testNotificationPercents.size, progress.notificationPercent.size)
        }

        @Test
        fun testEquals() {
            val progress1 = progress.copy(32442)
            val progress2 = progress.copy(32442)

            assertEquals(progress1, progress2)
            assertNotEquals(progress, progress1)
            assertNotEquals(progress, progress2)
        }

        @Test
        fun testEquals_OtherType() {
            assertNotEquals(progress, "")
        }

        @Test
        fun testEquals_Null() {
            assertNotEquals(progress, null)
        }

        @Test
        fun testHasCode() {
            val progress1 = progress.copy(32442)
            val progress2 = progress.copy(32442)

            assertEquals(progress1.hashCode(), progress2.hashCode())
            assertNotEquals(progress.hashCode(), progress1.hashCode())
            assertNotEquals(progress.hashCode(), progress2.hashCode())
        }
    }

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

        private val progress = Progress(
                id = 5345,
                title = "Test title",
                color = ProgressColor.COLOR_YELLOW,
                end = Date(endMills),
                start = Date(startMills),
                progressType = ProgressType.YEAR_PROGRESS,
                notificationPercent = arrayListOf()
        )

        @Test
        fun checkProgressPErcentages() {
            val decimalFormat = DecimalFormat("#.##")
            assertEquals(percent, decimalFormat.format(progress.percent(Date(now))).toFloat())
        }
    }
}