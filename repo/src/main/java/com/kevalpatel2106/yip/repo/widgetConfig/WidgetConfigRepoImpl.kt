package com.kevalpatel2106.yip.repo.widgetConfig

import com.kevalpatel2106.yip.entity.WidgetConfig
import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigTheme
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

    override fun saveWidgetIds(appWidgetId: Int) {
        val ids = getWidgetIds()
            .toMutableList()
            .apply { add(appWidgetId) }
            .distinct()
            .toIntArray()
        prefsProvider.savePreferences(WIDGET_IDS, ids.joinToString(SEPARATOR))
    }

    override fun deleteWidgetIds(widgetIdsToDelete: IntArray) {
        val remainingIds = getWidgetIds()
            .filter { !widgetIdsToDelete.contains(it) }
            .toIntArray()
        prefsProvider.savePreferences(WIDGET_IDS, remainingIds.joinToString(SEPARATOR))
    }

    override fun getWidgetConfig(appWidgetId: Int): WidgetConfig {
        val theme = prefsProvider.getStringFromPreference(getPrefKeyTheme(appWidgetId))
        val content = prefsProvider.getStringFromPreference(getPrefKeyContent(appWidgetId))
        return WidgetConfig(
            id = appWidgetId,
            theme = WidgetConfigTheme.values().firstOrNull { it.value == theme } ?: defaultTheme,
            content = WidgetConfigContent.values().firstOrNull { it.value == content }
                ?: defaultContent
        )
    }

    override fun saveWidgetConfig(
        appWidgetId: Int,
        content: WidgetConfigContent,
        theme: WidgetConfigTheme
    ) {
        prefsProvider.savePreferences(getPrefKeyContent(appWidgetId), content.value)
        prefsProvider.savePreferences(getPrefKeyTheme(appWidgetId), theme.value)
    }

    private fun getPrefKeyTheme(widgetId: Int) =
        widgetId.toString() + PREF_WIDGET_ID_PREFIX + "theme"

    private fun getPrefKeyContent(widgetId: Int) =
        widgetId.toString() + PREF_WIDGET_ID_PREFIX + "content"

    companion object {
        private val defaultTheme = WidgetConfigTheme.LIGHT
        private val defaultContent = WidgetConfigContent.PERCENT
        private const val SEPARATOR = ","
        private const val PREF_WIDGET_ID_PREFIX = "_widget_config_"
        private const val WIDGET_IDS = "widget_ids"
    }
}
