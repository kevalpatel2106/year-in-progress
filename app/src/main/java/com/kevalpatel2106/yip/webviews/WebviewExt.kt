package com.kevalpatel2106.yip.webviews

import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import com.kevalpatel2106.feature.core.BuildConfig

internal fun WebView.setUp(isDebug: Boolean = BuildConfig.DEBUG) {
    scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

    settings.loadsImagesAutomatically = true
    settings.javaScriptEnabled = false

    // Disable the cache for debug mode.
    settings.setAppCacheEnabled(isDebug)
    settings.cacheMode = if (isDebug) WebSettings.LOAD_DEFAULT else WebSettings.LOAD_NO_CACHE
}