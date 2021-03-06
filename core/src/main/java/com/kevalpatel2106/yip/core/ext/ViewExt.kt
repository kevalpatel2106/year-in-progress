package com.kevalpatel2106.yip.core.ext

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.appcompat.app.ActionBar
import androidx.core.graphics.drawable.DrawableCompat

private const val SLIDE_ANIMATION_DEFAULT_DURATION = 300L
private const val MENU_PROGRESSBAR_DIMEN_PX = 25

@SuppressLint("ObsoleteSdkInt")
fun ProgressBar.setProgressTint(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        val wrapDrawable = DrawableCompat.wrap(progressDrawable ?: indeterminateDrawable)
        DrawableCompat.setTint(
            wrapDrawable,
            color
        )
        progressDrawable = DrawableCompat.unwrap(wrapDrawable)
    } else {
        progressDrawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        indeterminateDrawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }
}

fun View.showOrHide(isShow: Boolean) {
    visibility = (if (isShow) View.VISIBLE else View.GONE)
}

fun MenuItem.showOrHideLoader(context: Context, isShow: Boolean) {
    if (isShow) {
        val progressBar = ProgressBar(context).apply {
            setPadding(
                MENU_PROGRESSBAR_DIMEN_PX,
                MENU_PROGRESSBAR_DIMEN_PX,
                MENU_PROGRESSBAR_DIMEN_PX,
                MENU_PROGRESSBAR_DIMEN_PX
            )
            setProgressTint(Color.WHITE)
        }
        actionView = progressBar
        isEnabled = false
    } else {
        actionView = null
        isEnabled = true
    }
}

fun View.slideUp(animationDuration: Long = SLIDE_ANIMATION_DEFAULT_DURATION) {
    val animate = TranslateAnimation(0f, 0f, height.toFloat(), 0f)
    startAnimation(animate.apply {
        duration = animationDuration
        fillAfter = true
        interpolator = DecelerateInterpolator()
    })
}

fun View.slideDown(animationDuration: Long = SLIDE_ANIMATION_DEFAULT_DURATION) {
    val animate = TranslateAnimation(0f, 0f, 0f, height.toFloat())
    startAnimation(animate.apply {
        duration = animationDuration
        fillAfter = true
        interpolator = DecelerateInterpolator()
    })
}

fun ActionBar.set(showHome: Boolean = true, showTitle: Boolean = true) {
    setDisplayShowTitleEnabled(showTitle)
    setDisplayShowHomeEnabled(showHome)
    setDisplayHomeAsUpEnabled(showHome)
}
