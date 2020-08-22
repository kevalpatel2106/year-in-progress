package com.kevalpatel2106.yip.widget.config

import android.appwidget.AppWidgetManager
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import com.kevalpatel2106.yip.R

data class WidgetConfigViewState(
    val widgetId: Int,
    @DrawableRes val previewImageRes: Int,
    @IdRes val selectedContentId: Int,
    @IdRes val selectedThemeId: Int,
    val applyButtonEnabled: Boolean
) {
    companion object {
        fun initialState() = WidgetConfigViewState(
            widgetId = AppWidgetManager.INVALID_APPWIDGET_ID,
            previewImageRes = R.drawable.list_widget_preview_percent_light,
            applyButtonEnabled = false,
            selectedThemeId = R.id.widget_config_light_theme_radio,
            selectedContentId = R.id.widget_config_percent_content_radio
        )
    }
}
