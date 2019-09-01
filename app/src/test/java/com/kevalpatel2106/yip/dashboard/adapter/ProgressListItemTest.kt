package com.kevalpatel2106.yip.dashboard.adapter

import android.graphics.drawable.GradientDrawable
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class ProgressListItemTest {
    private val testId = 8765L
    private val testTitle = "Test title"
    private val testColor = ProgressColor.COLOR_YELLOW
    private val testEndDate = Date(System.currentTimeMillis())
    private val testStartDate =
        Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))
    private val testProgressType = ProgressType.YEAR_PROGRESS
    private val testNotificationPercents = arrayListOf<Float>()

    private val progress = Progress(
        id = testId,
        title = testTitle,
        color = testColor,
        end = testEndDate,
        start = testStartDate,
        progressType = testProgressType,
        notificationPercent = testNotificationPercents,
        percent = 2f
    )

    private lateinit var progressListItem: ProgressListItem

    @Before
    fun before() {
        progressListItem = ProgressListItem(progress, "", GradientDrawable())
    }

    @Test
    fun checkListItemType() {
        Assert.assertEquals(ProgressAdapter.PROGRESS_BAR_TYPE, progressListItem.type)
    }

    @Test
    fun checkProgressValue() {
        Assert.assertEquals(progress, progressListItem.progress)
    }
}
