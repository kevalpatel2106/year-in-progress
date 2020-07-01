package com.kevalpatel2106.yip.webviews

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.core.BaseViewModel
import dagger.hilt.android.qualifiers.ApplicationContext

internal class WebViewViewModel @ViewModelInject constructor(
    @ApplicationContext private val application: Context
) : BaseViewModel() {

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

    @SuppressLint("ResourceType")
    fun submitLink(link: String?, @StringRes title: Int) {
        require(!(link == null || title <= 0)) { "Invalid link: $link or title: $title" }
        viewState.value = viewState.value?.copy(
            linkUrl = link,
            title = application.getString(title)
        )
    }
}
