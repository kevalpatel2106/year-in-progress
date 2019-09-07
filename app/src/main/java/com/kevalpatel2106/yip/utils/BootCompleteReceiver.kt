package com.kevalpatel2106.yip.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.notifications.ProgressNotificationReceiver
import com.kevalpatel2106.yip.repo.providers.AlarmProvider
import dagger.android.AndroidInjection
import javax.inject.Inject

internal class BootCompleteReceiver : BroadcastReceiver() {

    @Inject
    internal lateinit var alarmProvider: AlarmProvider

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED || context == null) return
        AndroidInjection.inject(this, context)
        alarmProvider.updateAlarms(ProgressNotificationReceiver::class.java)
    }
}
