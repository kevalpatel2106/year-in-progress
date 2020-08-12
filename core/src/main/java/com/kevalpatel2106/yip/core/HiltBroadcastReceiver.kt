package com.kevalpatel2106.yip.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper

/**
 * Temporary class until Hilt fixes [BroadcastReceiver] support
 * https://github.com/google/dagger/issues/1918
 */
abstract class HiltBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) = Unit
}
