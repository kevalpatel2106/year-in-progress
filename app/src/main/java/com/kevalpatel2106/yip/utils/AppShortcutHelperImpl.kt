package com.kevalpatel2106.yip.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.Progress
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
            application.getString(R.string.progress_pin_shortcut_id)
        ).setIcon(IconCompat.createWithResource(application, R.drawable.progress_app_shortcut))
            .setShortLabel(title)
            .setIntent(launchIntent)
            .setAlwaysBadged()
            .build()

        ShortcutManagerCompat.requestPinShortcut(application, shortcutInfo, null)
    }

    override fun updateDynamicShortcuts(progresses: List<Progress>): Boolean {
        ShortcutManagerCompat.removeAllDynamicShortcuts(application)
        if (progresses.isEmpty() || isDeviceSupportAppShortcuts(application)) return true

        val icon = IconCompat.createWithResource(application, R.drawable.progress_app_shortcut)
        val shortcuts = getProgressesFoAppShortcuts(progresses)
            .map { progress ->
                ShortcutInfoCompat.Builder(
                    application,
                    application.getString(R.string.progress_app_shortcut_id, progress.id)
                ).setIcon(icon)
                    .setShortLabel(progress.title)
                    .setIntent(AppLaunchHelper.launchWithProgressDetail(application, progress.id))
                    .build()
            }
        return ShortcutManagerCompat.addDynamicShortcuts(application, shortcuts)
    }

    private fun getProgressesFoAppShortcuts(
        progresses: List<Progress>,
        maxShortcutCount: Int = ShortcutManagerCompat.getMaxShortcutCountPerActivity(application)
    ): List<Progress> {
        val lastIndex = min(progresses.size, maxShortcutCount - NUMBER_OF_STATIC_APP_SHORTCUT)
        return progresses.subList(0, lastIndex)
    }

    private fun isDeviceSupportAppShortcuts(application: Context) =
        ShortcutManagerCompat.getMaxShortcutCountPerActivity(application) == 0

    companion object {
        private const val NUMBER_OF_STATIC_APP_SHORTCUT = 1
    }
}
