package com.kevalpatel2106.yip.entity

@Suppress("MagicNumber")
enum class DeadlineType(val key: Int, val color: DeadlineColor) {
    YEAR_DEADLINE(key = 8459, color = DeadlineColor.COLOR_BLUE),
    DAY_DEADLINE(key = 346, color = DeadlineColor.COLOR_PINK),
    WEEK_DEADLINE(key = 54532, color = DeadlineColor.COLOR_YELLOW),
    MONTH_DEADLINE(key = 123, color = DeadlineColor.COLOR_GREEN),
    QUARTER_DEADLINE(key = 4534, color = DeadlineColor.COLOR_TILL),
    CUSTOM(key = 3411, color = DeadlineColor.COLOR_PINK)
}

fun DeadlineType.isPreBuild() = this != DeadlineType.CUSTOM
fun DeadlineType.isRepeatable() = this != DeadlineType.CUSTOM
