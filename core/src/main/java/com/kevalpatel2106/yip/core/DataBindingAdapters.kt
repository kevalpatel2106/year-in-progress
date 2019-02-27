package com.kevalpatel2106.yip.core

import android.view.View
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter

@BindingAdapter("android:visibility")
fun visibility(view: View, show: Boolean) {
    view.showOrHide(show)
}

@BindingAdapter("app:darkerBackground")
fun darkerBackground(view: View, @ColorInt color: Int) {
    view.setBackgroundColor(darkenColor(color))
}

@BindingAdapter("app:setProgressTint")
fun setProgressTint(view: View, @ColorInt color: Int) {
    (view as? ProgressBar)?.setProgressTint(color)
}