package com.kevalpatel2106.yip.core

import android.view.View
import android.webkit.WebView
import android.widget.ViewFlipper
import androidx.databinding.BindingAdapter

@BindingAdapter("android:visibility")
fun visibility(view: View, show: Boolean) {
    view.showOrHide(show)
}

@BindingAdapter("app:loadUrl")
fun loadUrl(view: View, url: String) {
    if (url.isNotBlank()) (view as? WebView)?.loadUrl(url)
}

@BindingAdapter("app:displayChild")
@Suppress("CAST_NEVER_SUCCEEDS")
fun displayChild(view: View, pos: Int) {
    (view as? ViewFlipper)?.displayedChild = pos
}
