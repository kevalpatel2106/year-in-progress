package com.kevalpatel2106.yip.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.notifications.ProgressNotificationReceiver
import com.kevalpatel2106.yip.repo.providers.AlarmProvider
import dagger.android.AndroidInjection
import javax.inject.Inject

internal class SysEventsReceiver : BroadcastReceiver() {

    @Inject
    internal lateinit var alarmProvider: AlarmProvider

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.isValidIntent()) {
            AndroidInjection.inject(this, context)
            alarmProvider.updateAlarms(ProgressNotificationReceiver::class.java)
        }
    }

    private fun Intent.isValidIntent(): Boolean {
        return action == Intent.ACTION_BOOT_COMPLETED && action == Intent.ACTION_LOCALE_CHANGED
    }
}
