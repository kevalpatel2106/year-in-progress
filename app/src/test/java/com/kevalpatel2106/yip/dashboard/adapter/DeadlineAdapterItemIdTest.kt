package com.kevalpatel2106.yip.dashboard.adapter

import android.graphics.drawable.GradientDrawable
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.dashboard.adapter.listItem.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.DeadlineListItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.PaddingItem
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class DeadlineAdapterItemIdTest {
    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private lateinit var deadlineListItem: DeadlineListItem
    private lateinit var adapter: DeadlineAdapter

    @Before
    fun setUp() {
        deadlineListItem = DeadlineListItem(
            Deadline(
                id = kFixture(),
                title = kFixture(),
                color = DeadlineColor.COLOR_YELLOW,
                end = Date(
                    System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                        1,
                        TimeUnit.DAYS
                    )
                ),
                start = Date(System.currentTimeMillis()),
                deadlineType = DeadlineType.YEAR_DEADLINE,
                notificationPercent = kFixture(),
                percent = kFixture()
            ),
            kFixture(),
            GradientDrawable()
        )
        adapter = DeadlineAdapter(object : DeadlineAdapterEventListener {
            override fun onDeadlineClicked(deadline: Deadline) = Unit
        })
    }

    @Test
    fun `when adapter initialised check has table ids`() {
        assertTrue(adapter.hasStableIds())
    }

    @Test
    fun `when adapter has deadline list item check item id is same as deadline id`() {
        // when
        adapter.submitList(listOf(deadlineListItem))

        // check
        assertEquals(deadlineListItem.deadline.id, adapter.getItemId(0))
    }

    @Test
    fun `when adapter has ads list item check item id is multiplication of position and view type`() {
        // when
        adapter.submitList(listOf(deadlineListItem, deadlineListItem, AdsItem))

        // check
        assertEquals(AdsItem.type * 2L, adapter.getItemId(2))
    }

    @Test
    fun `when adapter has padding list item check item id is multiplication of position and view type`() {
        // when
        adapter.submitList(listOf(deadlineListItem, deadlineListItem, PaddingItem))

        //check
        assertEquals(PaddingItem.type * 2L, adapter.getItemId(2))
    }
}
