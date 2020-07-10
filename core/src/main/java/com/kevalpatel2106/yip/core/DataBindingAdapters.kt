package com.kevalpatel2106.yip.core

import android.view.View
import android.webkit.WebView
import android.widget.ViewFlipper
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("android:showOrHide")
fun visibility(view: View, show: Boolean) {
    view.showOrHide(show)
}

@BindingAdapter("app:loadUrl")
fun loadUrl(view: View, url: String) {
    if (url.isNotBlank()) (view as? WebView)?.loadUrl(url)
}

@BindingAdapter("app:displayChild")
fun displayChild(view: View, pos: Int) {
    (view as? ViewFlipper)?.displayedChild = pos
}

@BindingAdapter("android:enabled")
fun enableView(view: View, isEnabled: Boolean) {
    view.isEnabled = isEnabled
}

@BindingAdapter("app:errorText")
fun textInputLayoutErrorText(view: View, error: String) {
    (view as? TextInputLayout)?.error = error
}
