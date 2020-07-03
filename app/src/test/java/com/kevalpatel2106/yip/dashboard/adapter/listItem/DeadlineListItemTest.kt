package com.kevalpatel2106.yip.dashboard.adapter.listItem

import android.graphics.drawable.GradientDrawable
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.dashboard.adapter.DeadlineAdapter
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DeadlineListItemTest {
    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }

    private val deadline = Deadline(
        id = kFixture(),
        title = kFixture(),
        color = DeadlineColor.COLOR_BLUE,
        end = kFixture(),
        start = kFixture(),
        deadlineType = DeadlineType.DAY_DEADLINE,
        notificationPercent = kFixture(),
        percent = kFixture()
    )
    private val percentString = kFixture<String>()
    private val gradientDrawable = GradientDrawable()

    private lateinit var deadlineListItem: DeadlineListItem

    @Before
    fun before() {
        deadlineListItem = DeadlineListItem(deadline, percentString, gradientDrawable)
    }

    @Test
    fun `when init check value of deadline`() {
        assertEquals(deadline, deadlineListItem.deadline)
    }

    @Test
    fun `when init check value of percent string`() {
        assertEquals(percentString, deadlineListItem.percentString)
    }

    @Test
    fun `when init check value of gradient drawable`() {
        assertEquals(gradientDrawable, deadlineListItem.backgroundGradient)
    }

    @Test
    fun `check view holder type`() {
        assertEquals(DeadlineAdapter.TYPE_DEADLINE, deadlineListItem.type)
    }
}
