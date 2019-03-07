package com.kevalpatel2106.yip.dashboard.adapter

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AdsItemTest {
    private val adsItem = AdsItem

    @Test
    fun checkViewHolderType() {
        Assert.assertEquals(ProgressAdapter.ADS_TYPE, adsItem.type)
    }

}