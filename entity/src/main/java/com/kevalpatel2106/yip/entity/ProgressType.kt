package com.kevalpatel2106.yip.entity

import androidx.annotation.ColorRes

enum class ProgressType(val key: Int, @ColorRes val color: ProgressColor) {
    YEAR_PROGRESSType(key = 8459, color = ProgressColor.COLOR_BLUE),
    DAY_PROGRESSType(key = 346, color = ProgressColor.COLOR_BLUE),
    WEEK_PROGRESSType(key = 54532, color = ProgressColor.COLOR_YELLOW),
    MONTH_PROGRESSType(key = 123, color = ProgressColor.COLOR_GREEN),
    QUARTER_PROGRESSType(key = 4534, color = ProgressColor.COLOR_GREY),
    CUSTOM(key = 3411, color = ProgressColor.COLOR_BLUE)
}

fun ProgressType.isPreBuild() = this != ProgressType.CUSTOM