package com.kevalpatel2106.yip.entity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class DeadlineTest {
    private val testId = 8765L
    private val testTitle = "Test title"
    private val testColor = DeadlineColor.COLOR_YELLOW
    private val testEndDate = Date(System.currentTimeMillis())
    private val testStartDate =
        Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))
    private val testType = DeadlineType.YEAR_DEADLINE
    private val testPercent = 2f
    private val testNotificationPercents = arrayListOf<Float>()

    private val deadline = Deadline(
        id = testId,
        title = testTitle,
        color = testColor,
        end = testEndDate,
        start = testStartDate,
        deadlineType = testType,
        notificationPercent = testNotificationPercents,
        percent = testPercent
    )

    @Test
    fun testConstructors() {
        assertEquals(testId, deadline.id)
        assertEquals(testTitle, deadline.title)
        assertEquals(testColor, deadline.color)
        assertEquals(testEndDate, deadline.end)
        assertEquals(testStartDate, deadline.start)
        assertEquals(testType, deadline.deadlineType)
        assertEquals(testNotificationPercents.size, deadline.notificationPercent.size)
        assertEquals(testPercent, deadline.percent)
    }

    @Test
    fun testEquals_OtherType() {
        assertNotEquals(123423L, deadline)
    }

    @Test
    fun testEquals_Null() {
        assertNotEquals(deadline, null)
    }
}
