package com.kevalpatel2106.yip.entity

import android.graphics.Color
import androidx.annotation.ColorInt

enum class DeadlineColor(@ColorInt val colorInt: Int) {
    COLOR_ORANGE(colorInt = Color.parseColor("#EBA14B")),
    COLOR_BLUE(colorInt = Color.parseColor("#61719F")),
    COLOR_YELLOW(colorInt = Color.parseColor("#F1BB41")),
    COLOR_GREEN(colorInt = Color.parseColor("#56835D")),
    COLOR_TILL(colorInt = Color.parseColor("#7EC4BB")),
    COLOR_PINK(colorInt = Color.parseColor("#ED7A6C")),
    COLOR_GRAY(colorInt = Color.GRAY)
}
