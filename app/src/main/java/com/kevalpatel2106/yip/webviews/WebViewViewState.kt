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

internal enum class WebviewFlipperPosition(val value: Int) {
    POS_ERROR(2),
    POS_LOADING(0),
    POS_WEBVIEW(1)
}