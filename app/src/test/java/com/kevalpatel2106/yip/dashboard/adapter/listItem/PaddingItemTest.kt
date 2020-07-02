package com.kevalpatel2106.yip.dashboard.adapter.listItem

import com.kevalpatel2106.yip.dashboard.adapter.DeadlineAdapter
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PaddingItemTest {
    private val paddingItem = PaddingItem

    @Test
    fun checkViewHolderType() {
        Assert.assertEquals(DeadlineAdapter.TYPE_PADDING, paddingItem.type)
    }
}
