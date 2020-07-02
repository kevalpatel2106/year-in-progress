package com.kevalpatel2106.yip.utils

import android.content.Intent
import com.kevalpatel2106.yip.entity.Deadline

interface AppShortcutHelper {
    fun requestPinShortcut(title: String, launchIntent: Intent)
    fun updateDynamicShortcuts(deadlines: List<Deadline>): Boolean
}
