package com.kevalpatel2106.yip.webviews

import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WebviewUsecaseKtTest {

    private lateinit var webView: WebView

    @Before
    fun before() {
        webView = WebView(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun checkScrollbarStyle() {
        webView.setUp()
        Assert.assertEquals(View.SCROLLBARS_INSIDE_OVERLAY, webView.scrollBarStyle)
    }

    @Test
    fun checkLoadImagesEnabled() {
        webView.setUp()
        Assert.assertTrue(webView.settings.loadsImagesAutomatically)
    }

    @Test
    fun checkJavaScriptNotEnable() {
        webView.setUp()
        Assert.assertFalse(webView.settings.javaScriptEnabled)
    }

    @Test
    fun whenAppIsInDebug_checkCacheIsEnabled() {
        webView.setUp(true)
        Assert.assertEquals(WebSettings.LOAD_DEFAULT, webView.settings.cacheMode)
    }

    @Test
    fun whenAppIsNotDebug_checkCacheIsDisabled() {
        webView.setUp(false)
        Assert.assertEquals(WebSettings.LOAD_NO_CACHE, webView.settings.cacheMode)
    }
}
