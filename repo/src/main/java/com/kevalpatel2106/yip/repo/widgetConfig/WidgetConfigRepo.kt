package com.kevalpatel2106.yip.repo.widgetConfig

import com.kevalpatel2106.yip.entity.WidgetConfig
import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigTheme

interface WidgetConfigRepo {
    fun getWidgetIds(): IntArray
    fun saveWidgetIds(widgetId: Int)
    fun deleteWidgetIds(widgetIds: IntArray)
    fun getWidgetConfig(widgetId: Int): WidgetConfig
    fun saveWidgetConfig(widgetId: Int, content: WidgetConfigContent, theme: WidgetConfigTheme)
}
