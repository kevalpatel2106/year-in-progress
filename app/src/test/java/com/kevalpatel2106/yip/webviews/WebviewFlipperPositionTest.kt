package com.kevalpatel2106.yip.webviews

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
internal class WebviewFlipperPositionTest(
    private val position: WebviewFlipperPosition,
    private val value: Int
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(WebviewFlipperPosition.POS_ERROR, 2),
                arrayOf(WebviewFlipperPosition.POS_LOADING, 0),
                arrayOf(WebviewFlipperPosition.POS_WEBVIEW, 1)
            )
        }
    }

    @Test
    fun checkWebViewPositionValue() {
        Assert.assertEquals(value, position.value)
    }
}