package com.kevalpatel2106.yip.webviews

import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import com.kevalpatel2106.yip.BuildConfig

internal fun WebView.setUp() {
    scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

    settings.loadsImagesAutomatically = true
    settings.javaScriptEnabled = false

    // Disable the cache for debug mode.
    settings.setAppCacheEnabled(!BuildConfig.DEBUG)
    settings.cacheMode = if (BuildConfig.DEBUG) WebSettings.LOAD_NO_CACHE else WebSettings.LOAD_DEFAULT
}