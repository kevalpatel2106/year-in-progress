package com.kevalpatel2106.yip.utils

import android.content.Intent
import com.kevalpatel2106.yip.entity.Progress

interface AppShortcutHelper {
    fun requestPinShortcut(title: String, launchIntent: Intent)
    fun updateDynamicShortcuts(progresses: List<Progress>): Boolean
}
