package com.kevalpatel2106.yip.dashboard.adapter.listItem

import android.graphics.drawable.GradientDrawable
import com.kevalpatel2106.yip.core.emptyString
import com.kevalpatel2106.yip.dashboard.adapter.DeadlineAdapter
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class DeadlineListItemTest {
    private val testId = 8765L
    private val testTitle = "Test title"
    private val testColor = DeadlineColor.COLOR_YELLOW
    private val testEndDate = Date(System.currentTimeMillis())
    private val testStartDate =
        Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))
    private val testType = DeadlineType.YEAR_DEADLINE
    private val testDeadlinePercent = 2f
    private val testNotificationPercents = arrayListOf<Float>()

    private val deadline = Deadline(
        id = testId,
        title = testTitle,
        color = testColor,
        end = testEndDate,
        start = testStartDate,
        deadlineType = testType,
        notificationPercent = testNotificationPercents,
        percent = testDeadlinePercent
    )

    private lateinit var deadlineListItem: DeadlineListItem

    @Before
    fun before() {
        deadlineListItem = DeadlineListItem(deadline, emptyString(), GradientDrawable())
    }

    @Test
    fun checkListItemType() {
        Assert.assertEquals(DeadlineAdapter.TYPE_DEADLINE, deadlineListItem.type)
    }

    @Test
    fun checkDeadlineValue() {
        Assert.assertEquals(deadline, deadlineListItem.deadline)
    }

    @Test
    fun checkViewHolderType() {
        Assert.assertEquals(DeadlineAdapter.TYPE_DEADLINE, deadlineListItem.type)
    }
}
