package com.kevalpatel2106.yip.webviews

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WebViewViewStateTest {
    private val testTitle = "xyz"
    private val testLink = "www.example.com"

    @Test
    fun checkScreenTitle() {
        val state = WebViewViewState(testTitle, testLink)
        assertEquals(testTitle, state.title)
    }

    @Test
    fun checkLink() {
        val state = WebViewViewState(testTitle, testLink)
        assertEquals(testLink, state.linkUrl)
    }

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
        val stateWithDifferentArgs = WebViewViewState(testTitle, testLink)

        assertEquals(state, state1)
        assertNotEquals(state, stateWithDifferentArgs)
        assertNotEquals(state1, stateWithDifferentArgs)
    }
}
