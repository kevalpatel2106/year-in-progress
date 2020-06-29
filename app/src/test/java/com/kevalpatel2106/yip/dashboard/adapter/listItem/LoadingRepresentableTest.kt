package com.kevalpatel2106.yip.dashboard.adapter.listItem

import com.kevalpatel2106.yip.dashboard.adapter.ProgressAdapter
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoadingRepresentableTest {

    @Test
    fun checkType() {
        Assert.assertEquals(ProgressAdapter.TYPE_LOADING, LoadingRepresentable.type)
    }
}
