package com.kevalpatel2106.yip.widget.config

import android.appwidget.AppWidgetManager
import com.kevalpatel2106.yip.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WidgetConfigViewInitialStateTest {

    private val initialState = WidgetConfigViewState.initialState()

    @Test
    fun `given initial state check widget id invalid`() {
        assertEquals(AppWidgetManager.INVALID_APPWIDGET_ID, initialState.widgetId)
    }

    @Test
    fun `given initial state check preview image res`() {
        assertEquals(R.drawable.list_widget_preview_percent_light, initialState.previewImageRes)
    }

    @Test
    fun `given initial state check widget theme is light`() {
        assertEquals(R.id.widget_config_light_theme_radio, initialState.selectedThemeId)
    }

    @Test
    fun `given initial state check widget content is percent`() {
        assertEquals(R.id.widget_config_percent_content_radio, initialState.selectedContentId)
    }

    @Test
    fun `given initial state check apply button disabled`() {
        assertFalse(initialState.applyButtonEnabled)
    }
}
