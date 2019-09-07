package com.kevalpatel2106.yip.core.recyclerview.representable

import com.kevalpatel2106.yip.core.recyclerview.YipAdapter
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoadingRepresentableTest {

    @Test
    fun checkType() {
        Assert.assertEquals(YipAdapter.TYPE_LOADING, LoadingRepresentable.type)
    }
}
