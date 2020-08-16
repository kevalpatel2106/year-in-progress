package com.kevalpatel2106.yip.repo.widgetConfig

import com.kevalpatel2106.yip.entity.WidgetConfig
import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigTheme

interface WidgetConfigRepo {
    fun getWidgetIds(): IntArray
    fun saveWidgetIds(appWidgetId: Int)
    fun deleteWidgetIds(appWidgetIds: IntArray)
    fun getWidgetConfig(appWidgetId: Int): WidgetConfig
    fun saveWidgetConfig(appWidgetId: Int, content: WidgetConfigContent, theme: WidgetConfigTheme)
}
