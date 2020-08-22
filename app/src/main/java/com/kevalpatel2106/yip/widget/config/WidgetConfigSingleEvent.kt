package com.kevalpatel2106.yip.widget.config

sealed class WidgetConfigSingleEvent {
    data class CloseScreen(val resultCode: Int, val appWidgetId: Int) :
        WidgetConfigSingleEvent()
}
