package com.kevalpatel2106.yip.entity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class ProgressTest {
    private val testId = 8765L
    private val testTitle = "Test title"
    private val testColor = ProgressColor.COLOR_YELLOW
    private val testEndDate = Date(System.currentTimeMillis())
    private val testStartDate =
        Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))
    private val testProgressType = ProgressType.YEAR_PROGRESS
    private val testProgress = 2f
    private val testNotificationPercents = arrayListOf<Float>()

    private val progress = Progress(
        id = testId,
        title = testTitle,
        color = testColor,
        end = testEndDate,
        start = testStartDate,
        progressType = testProgressType,
        notificationPercent = testNotificationPercents,
        percent = testProgress
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
        assertEquals(testProgress, progress.percent)
    }

    @Test
    fun testEquals() {
        val progress1 = progress.copy(32442)
        val progress2 = progress1.copy(color = ProgressColor.COLOR_YELLOW)
        val progress3 = progress.copy(percent = 1f)

        assertEquals(progress1, progress2)
        assertNotEquals(progress, progress3)
        assertNotEquals(progress, progress1)
        assertNotEquals(progress, progress2)
    }

    @Test
    fun testEquals_OtherType() {
        assertNotEquals(123423L, progress)
    }

    @Test
    fun testEquals_Null() {
        assertNotEquals(progress, null)
    }

    @Test
    fun testHasCode() {
        val progressWithOtherId = progress.copy(32442)
        val progressWithOtherColor = progressWithOtherId.copy(color = ProgressColor.COLOR_YELLOW)
        val progressWithOtherPercent = progress.copy(percent = 1f)

        assertEquals(progressWithOtherId.hashCode(), progressWithOtherColor.hashCode())
        assertNotEquals(progress.hashCode(), progressWithOtherPercent.hashCode())
        assertNotEquals(progress.hashCode(), progressWithOtherId.hashCode())
        assertNotEquals(progress.hashCode(), progressWithOtherColor.hashCode())
    }
}
