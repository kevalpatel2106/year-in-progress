package com.kevalpatel2106.yip.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.Deadline
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.min

internal class AppShortcutHelperImpl @Inject constructor(
    @ApplicationContext private val application: Context
) : AppShortcutHelper {

    override fun requestPinShortcut(title: String, launchIntent: Intent) {
        if (!ShortcutManagerCompat.isRequestPinShortcutSupported(application)) return

        val shortcutInfo = ShortcutInfoCompat.Builder(
            application,
            application.getString(R.string.deadline_pin_shortcut_id)
        ).setIcon(IconCompat.createWithResource(application, R.drawable.deadline_app_shortcut))
            .setShortLabel(title)
            .setIntent(launchIntent)
            .setAlwaysBadged()
            .build()

        ShortcutManagerCompat.requestPinShortcut(application, shortcutInfo, null)
    }

    override fun updateDynamicShortcuts(deadlines: List<Deadline>): Boolean {
        ShortcutManagerCompat.removeAllDynamicShortcuts(application)
        if (deadlines.isEmpty() || isDeviceSupportAppShortcuts(application)) return true

        val icon = IconCompat.createWithResource(application, R.drawable.deadline_app_shortcut)
        val shortcuts = getDeadlinesFoAppShortcuts(deadlines)
            .map { deadline ->
                ShortcutInfoCompat.Builder(
                    application,
                    application.getString(R.string.deadline_app_shortcut_id, deadline.id)
                ).setIcon(icon)
                    .setShortLabel(deadline.title)
                    .setIntent(AppLaunchHelper.launchWithDeadlineDetail(application, deadline.id))
                    .build()
            }
        return ShortcutManagerCompat.addDynamicShortcuts(application, shortcuts)
    }

    private fun getDeadlinesFoAppShortcuts(
        deadlines: List<Deadline>,
        maxShortcutCount: Int = ShortcutManagerCompat.getMaxShortcutCountPerActivity(application)
    ): List<Deadline> {
        val lastIndex = min(deadlines.size, maxShortcutCount - NUMBER_OF_STATIC_APP_SHORTCUT)
        return deadlines.subList(0, lastIndex)
    }

    private fun isDeviceSupportAppShortcuts(application: Context) =
        ShortcutManagerCompat.getMaxShortcutCountPerActivity(application) == 0

    companion object {
        private const val NUMBER_OF_STATIC_APP_SHORTCUT = 1
    }
}
