package com.kevalpatel2106.yip.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.notifications.ProgressNotificationReceiver
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import dagger.android.AndroidInjection
import javax.inject.Inject

internal class SysEventsReceiver : BroadcastReceiver() {

    @Inject
    internal lateinit var alarmRepo: AlarmRepo

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.isValidIntent()) {
            AndroidInjection.inject(this, context)
            alarmRepo.updateAlarms(ProgressNotificationReceiver::class.java)
        }
    }

    private fun Intent.isValidIntent(): Boolean {
        return action == Intent.ACTION_BOOT_COMPLETED && action == Intent.ACTION_LOCALE_CHANGED
    }
}
