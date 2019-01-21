package com.kevalpatel2106.yip.core

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

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
        progressDrawable?.setColorFilter(
                color,
                PorterDuff.Mode.SRC_IN
        )
        indeterminateDrawable?.setColorFilter(
                color,
                PorterDuff.Mode.SRC_IN
        )
    }
}

fun View.showOrHide(isShow: Boolean) {
    visibility = if (isShow) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@ColorInt
fun darkenColor(@ColorInt color: Int, factor: Float = 0.3f): Int {
    return Color.HSVToColor(FloatArray(3).apply {
        Color.colorToHSV(color, this)
        this[2] *= factor
    })
}

fun MenuItem.showOrHideLoader(context: Context, isShow: Boolean) {
    if (isShow) {
        val progressBar = ProgressBar(context).apply {
            setPadding(25, 25, 25, 25)
            setProgressTint(Color.WHITE)
        }
        actionView = progressBar
        isEnabled = false
    } else {
        actionView = null
        isEnabled = true
    }
}