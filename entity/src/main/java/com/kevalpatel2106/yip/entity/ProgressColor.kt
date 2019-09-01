package com.kevalpatel2106.yip.entity

import android.graphics.Color
import androidx.annotation.ColorInt

enum class ProgressColor(@ColorInt val value: Int) {
    COLOR_ORANGE(value = Color.parseColor("#EBA14B")),
    COLOR_BLUE(value = Color.parseColor("#61719F")),
    COLOR_YELLOW(value = Color.parseColor("#F1BB41")),
    COLOR_GREEN(value = Color.parseColor("#56835D")),
    COLOR_TILL(value = Color.parseColor("#7EC4BB")),
    COLOR_PINK(value = Color.parseColor("#ED7A6C"))
}

fun getProgressColor(@ColorInt value: Int): ProgressColor {
    return ProgressColor.values().firstOrNull { it.value == value } ?: ProgressColor.COLOR_BLUE
}

fun isValidProgressColor(@ColorInt value: Int?): Boolean {
    return ProgressColor.values().firstOrNull { it.value == value } != null
}
