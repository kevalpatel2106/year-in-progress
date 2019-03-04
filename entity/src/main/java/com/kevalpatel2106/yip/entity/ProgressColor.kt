package com.kevalpatel2106.yip.entity

import android.graphics.Color
import androidx.annotation.ColorInt

enum class ProgressColor(@ColorInt val value: Int) {
    COLOR_ORANGE(value = Color.parseColor("#FF5722")),
    COLOR_BLUE(value = Color.parseColor("#3F51B5")),
    COLOR_YELLOW(value = Color.parseColor("#FBC02D")),
    COLOR_GREEN(value = Color.parseColor("#8BC34A")),
    COLOR_GREY(value = Color.parseColor("#FF9800"))
}

fun getProgressColor(@ColorInt value: Int): ProgressColor {
    return ProgressColor.values().firstOrNull { it.value == value } ?: ProgressColor.COLOR_BLUE
}

fun isValidProgressColor(@ColorInt value: Int?): Boolean {
    return ProgressColor.values().firstOrNull { it.value == value } != null
}