package com.kevalpatel2106.yip.entity

@Suppress("MagicNumber")
enum class ProgressType(val key: Int, val color: ProgressColor) {
    YEAR_PROGRESS(key = 8459, color = ProgressColor.COLOR_BLUE),
    DAY_PROGRESS(key = 346, color = ProgressColor.COLOR_PINK),
    WEEK_PROGRESS(key = 54532, color = ProgressColor.COLOR_YELLOW),
    MONTH_PROGRESS(key = 123, color = ProgressColor.COLOR_GREEN),
    QUARTER_PROGRESS(key = 4534, color = ProgressColor.COLOR_TILL),
    CUSTOM(key = 3411, color = ProgressColor.COLOR_PINK)
}

fun ProgressType.isPreBuild() = this != ProgressType.CUSTOM
