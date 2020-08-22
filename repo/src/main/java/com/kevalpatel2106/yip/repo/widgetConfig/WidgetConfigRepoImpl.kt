package com.kevalpatel2106.yip.repo.widgetConfig

import androidx.annotation.VisibleForTesting
import com.kevalpatel2106.yip.entity.WidgetConfig
import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigTheme
import com.kevalpatel2106.yip.entity.ext.getWidgetConfigContent
import com.kevalpatel2106.yip.entity.ext.getWidgetConfigTheme
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import javax.inject.Inject

internal class WidgetConfigRepoImpl @Inject constructor(
    private val prefsProvider: SharedPrefsProvider
) : WidgetConfigRepo {

    override fun getWidgetIds(): IntArray {
        return (prefsProvider.getStringFromPreference(WIDGET_IDS) ?: "")
            .split(SEPARATOR)
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .toIntArray()
    }

    override fun saveWidgetIds(widgetId: Int) {
        val ids = getWidgetIds()
            .toMutableList()
            .apply { add(widgetId) }
            .distinct()
            .toIntArray()
        prefsProvider.savePreferences(WIDGET_IDS, ids.joinToString(SEPARATOR))
    }

    override fun deleteWidgetIds(widgetIds: IntArray) {
        val remainingIds = getWidgetIds()
            .filter { !widgetIds.contains(it) }
            .toIntArray()
        prefsProvider.savePreferences(WIDGET_IDS, remainingIds.joinToString(SEPARATOR))
    }

    override fun getWidgetConfig(widgetId: Int): WidgetConfig {
        val theme = prefsProvider.getStringFromPreference(getPrefKeyTheme(widgetId))
        val content = prefsProvider.getStringFromPreference(getPrefKeyContent(widgetId))
        return WidgetConfig(widgetId, getWidgetConfigTheme(theme), getWidgetConfigContent(content))
    }

    override fun saveWidgetConfig(
        widgetId: Int,
        content: WidgetConfigContent,
        theme: WidgetConfigTheme
    ) {
        prefsProvider.savePreferences(getPrefKeyContent(widgetId), content.value)
        prefsProvider.savePreferences(getPrefKeyTheme(widgetId), theme.value)
    }

    private fun getPrefKeyTheme(widgetId: Int): String =
        widgetId.toString() + PREF_WIDGET_ID_PREFIX + "theme"

    private fun getPrefKeyContent(widgetId: Int): String =
        widgetId.toString() + PREF_WIDGET_ID_PREFIX + "content"

    companion object {
        @VisibleForTesting
        const val SEPARATOR = ","

        @VisibleForTesting
        const val PREF_WIDGET_ID_PREFIX = "_widget_config_"

        @VisibleForTesting
        const val WIDGET_IDS = "widget_ids"
    }
}
