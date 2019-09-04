package com.kevalpatel2106.yip.webviews

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WebviewActivityArgsTest {

    private val testTitleRes = 8394
    private val testLink = "http://example.com"

    private val webviewActivityArgs = WebviewActivityArgs(testTitleRes, testLink)

    @Test
    fun checkTitleRes() {
        assertEquals(testTitleRes, webviewActivityArgs.titleRes)
    }

    @Test
    fun checkLink() {
        assertEquals(testLink, webviewActivityArgs.link)
    }

    @Test
    fun checkHashcode() {
        val withDifferentTitle = webviewActivityArgs.copy(titleRes = 7326)
        val withDifferentLink = webviewActivityArgs.copy(link = "http://example1.com")
        val sameArgs = WebviewActivityArgs(testTitleRes, testLink)

        assertEquals(sameArgs.hashCode(), webviewActivityArgs.hashCode())
        assertNotEquals(withDifferentTitle.hashCode(), webviewActivityArgs.hashCode())
        assertNotEquals(withDifferentLink.hashCode(), webviewActivityArgs.hashCode())
    }

    @Test
    fun checkEquals() {
        val withDifferentTitle = webviewActivityArgs.copy(titleRes = 7326)
        val withDifferentLink = webviewActivityArgs.copy(link = "http://example1.com")
        val sameArgs = WebviewActivityArgs(testTitleRes, testLink)

        assertEquals(sameArgs, webviewActivityArgs)
        assertNotEquals(withDifferentTitle, webviewActivityArgs)
        assertNotEquals(withDifferentLink, webviewActivityArgs)
    }
}
