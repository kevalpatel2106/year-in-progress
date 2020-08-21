package com.kevalpatel2106.yip.entity.ext

import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigContent.PERCENT

fun getWidgetConfigContent(value: String?): WidgetConfigContent {
    return WidgetConfigContent.values().firstOrNull { it.value == value } ?: PERCENT
}
