package com.kevalpatel2106.yip.webviews

internal data class WebViewViewState(
    val title: String,
    val linkUrl: String
) {

    companion object {
        fun initialState(): WebViewViewState {
            return WebViewViewState(
                title = "",
                linkUrl = ""
            )
        }
    }
}
