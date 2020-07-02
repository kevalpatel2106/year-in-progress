package com.kevalpatel2106.yip.dashboard.adapter

import android.graphics.drawable.GradientDrawable
import android.widget.LinearLayout
import com.kevalpatel2106.yip.core.emptyString
import com.kevalpatel2106.yip.dashboard.adapter.listItem.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.DeadlineListItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.PaddingItem
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class DeadlineAdapterTest {
    private val adapter = DeadlineAdapter(object : DeadlineAdapterEventListener {
        override fun onDeadlineClicked(deadline: Deadline) = Unit
    })
    private val deadlineListItem = DeadlineListItem(
        Deadline(
            id = 8765L,
            title = "Test title",
            color = DeadlineColor.COLOR_YELLOW,
            end = Date(
                System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                    1,
                    TimeUnit.DAYS
                )
            ),
            start = Date(System.currentTimeMillis()),
            deadlineType = DeadlineType.YEAR_DEADLINE,
            notificationPercent = arrayListOf(),
            percent = 2f
        ),
        emptyString(),
        GradientDrawable()
    )

    @Test
    fun checkSetHasStableIds() {
        assertTrue(adapter.hasStableIds())
    }

    @Test
    fun whenDeadlineItemGiven_checkGetItemId() {
        adapter.submitList(listOf(deadlineListItem))
        assertEquals(deadlineListItem.deadline.id, adapter.getItemId(0))
    }

    @Test
    fun whenAdsItem_checkGetItemId() {
        adapter.submitList(
            listOf(
                deadlineListItem, deadlineListItem,
                AdsItem
            )
        )
        assertEquals(AdsItem.type * 2L, adapter.getItemId(2))
    }

    @Test
    fun whenPaddingItem_checkGetItemId() {
        adapter.submitList(
            listOf(
                deadlineListItem, deadlineListItem, deadlineListItem,
                PaddingItem
            )
        )
        assertEquals(PaddingItem.type * 3L, adapter.getItemId(3))
    }

    @Test
    fun checkGetViewHolder_whenInvalidViewType() {
        try {
            adapter.createViewHolder(LinearLayout(RuntimeEnvironment.application), 3490)
            fail()
        } catch (e: IllegalStateException) {
            assertNotNull(e.message)
        }
    }
}
