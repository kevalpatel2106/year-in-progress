package com.kevalpatel2106.yip.dashboard.adapter

import android.graphics.drawable.GradientDrawable
import com.kevalpatel2106.yip.core.emptyString
import com.kevalpatel2106.yip.dashboard.adapter.adsType.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.paddingType.PaddingItem
import com.kevalpatel2106.yip.dashboard.adapter.progressType.ProgressListItem
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class ProgressAdapterTest {
    private val clickListener = { progress: Progress -> Unit }
    private val adapter = ProgressAdapter(clickListener)
    private val progressItem = ProgressListItem(
        Progress(
            id = 8765L,
            title = "Test title",
            color = ProgressColor.COLOR_YELLOW,
            end = Date(
                System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                    1,
                    TimeUnit.DAYS
                )
            ),
            start = Date(System.currentTimeMillis()),
            progressType = ProgressType.YEAR_PROGRESS,
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
    fun whenProgressItem_checkGetItemId() {
        adapter.submitList(listOf(progressItem))
        assertEquals(progressItem.progress.id, adapter.getItemId(0))
    }

    @Test
    fun whenAdsItem_checkGetItemId() {
        adapter.submitList(listOf(progressItem, progressItem, AdsItem))
        assertEquals(AdsItem.type * 2L, adapter.getItemId(2))
    }

    @Test
    fun whenPaddingItem_checkGetItemId() {
        adapter.submitList(listOf(progressItem, progressItem, progressItem, PaddingItem))
        assertEquals(PaddingItem.type * 3L, adapter.getItemId(3))
    }
}
