package com.kevalpatel2106.yip.webviews

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WebViewViewStateTest {

    @Test
    fun checkInitState() {
        val initState = WebViewViewState.initialState()
        Assert.assertTrue(initState.linkUrl.isEmpty())
        Assert.assertTrue(initState.title.isEmpty())
    }

    @Test
    fun checkEquals() {
        val state = WebViewViewState.initialState()
        val state1 = WebViewViewState.initialState()
        val state2 = WebViewViewState("xyz", "abc")

        Assert.assertEquals(state, state1)
        Assert.assertNotEquals(state, state2)
        Assert.assertNotEquals(state1, state2)
    }
}