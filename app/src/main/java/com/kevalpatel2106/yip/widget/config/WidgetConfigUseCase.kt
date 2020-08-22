package com.kevalpatel2106.yip.widget.config

import androidx.annotation.DrawableRes
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigTheme

object WidgetConfigUseCase {
    @DrawableRes
    fun getPreviewImage(content: WidgetConfigContent, theme: WidgetConfigTheme): Int {
        return if (content == WidgetConfigContent.PERCENT && theme == WidgetConfigTheme.LIGHT) {
            R.drawable.list_widget_preview_percent_light
        } else if (content == WidgetConfigContent.TIME_LEFT && theme == WidgetConfigTheme.LIGHT) {
            R.drawable.list_widget_preview_time_left_light
        } else if (content == WidgetConfigContent.PERCENT && theme == WidgetConfigTheme.DARK) {
            R.drawable.list_widget_preview_percent_dark
        } else {
            R.drawable.list_widget_preview_time_left_dark
        }
    }
}
