package com.kevalpatel2106.yip.entity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class ProgressTest {

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

    @Test
    fun testPercent() {
        assertEquals(100f, progress.percent(Date(System.currentTimeMillis())))
    }
}