package com.kevalpatel2106.yip.webviews

import com.kevalpatel2106.yip.core.ext.emptyString

internal data class WebViewViewState(val title: String, val linkUrl: String) {
    companion object {
        fun initialState(): WebViewViewState {
            return WebViewViewState(title = emptyString(), linkUrl = emptyString())
        }
    }
}
