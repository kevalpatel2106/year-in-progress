package com.kevalpatel2106.yip.entity.ext

import com.kevalpatel2106.yip.entity.WidgetConfigTheme

fun getWidgetConfigTheme(value: String?): WidgetConfigTheme {
    return WidgetConfigTheme.values().firstOrNull { it.value == value } ?: WidgetConfigTheme.LIGHT
}
