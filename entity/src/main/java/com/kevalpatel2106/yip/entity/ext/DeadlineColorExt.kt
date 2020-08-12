package com.kevalpatel2106.yip.entity.ext

import androidx.annotation.ColorInt
import com.kevalpatel2106.yip.entity.DeadlineColor

fun getDeadlineColor(@ColorInt value: Int): DeadlineColor {
    return DeadlineColor.values().firstOrNull { it.colorInt == value } ?: DeadlineColor.COLOR_GRAY
}
