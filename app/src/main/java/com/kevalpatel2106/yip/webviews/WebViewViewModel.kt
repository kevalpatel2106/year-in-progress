package com.kevalpatel2106.yip.webviews

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.core.BaseViewModel
import javax.inject.Inject

internal class WebViewViewModel @Inject constructor(private val application: Application) : BaseViewModel() {
    val viewState = MutableLiveData<WebViewViewState>()
    val flipperPosition = MutableLiveData<Int>()

    private var link: String = ""
    private var title: String = ""

    fun onPageLoaded() {
        flipperPosition.value = POS_WEBVIEW
    }

    fun onPageLoadingFailed() {
        flipperPosition.value = POS_ERROR
    }

    @SuppressLint("ResourceType")
    fun submitLink(link: String?, @StringRes title: Int) {
        if (link == null || title <= 0)
            throw IllegalArgumentException("Invalid link: $link or title: $title")
        this.link = link
        this.title = application.getString(title)
        loadPage()
    }

    fun loadPage() {
        viewState.value = WebViewViewState(
                linkUrl = this.link,
                title = this.title
        )
        flipperPosition.value = POS_LOADER
    }

    companion object {
        private const val POS_LOADER = 0
        private const val POS_WEBVIEW = 1
        private const val POS_ERROR = 2
    }
}
