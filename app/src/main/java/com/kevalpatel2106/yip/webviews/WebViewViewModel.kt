package com.kevalpatel2106.yip.webviews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.livedata.modify

internal class WebViewViewModel @ViewModelInject constructor() : BaseViewModel() {

    val viewState = MutableLiveData<WebViewViewState>(WebViewViewState.initialState())
    val flipperPosition = MutableLiveData<WebviewFlipperPosition>(
        WebviewFlipperPosition.POS_LOADING
    )

    fun onPageLoaded() {
        flipperPosition.value = WebviewFlipperPosition.POS_WEBVIEW
    }

    fun onPageLoadingFailed() {
        flipperPosition.value = WebviewFlipperPosition.POS_ERROR
    }

    fun reload() {
        flipperPosition.value = WebviewFlipperPosition.POS_LOADING
    }

    fun submitLink(link: String, title: String) {
        viewState.modify { copy(linkUrl = link, title = title) }
        flipperPosition.value = WebviewFlipperPosition.POS_LOADING
    }
}
