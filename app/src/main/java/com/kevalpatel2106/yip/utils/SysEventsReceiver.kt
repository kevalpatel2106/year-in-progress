package com.kevalpatel2106.yip.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.core.HiltBroadcastReceiver
import com.kevalpatel2106.yip.notifications.DeadlineNotificationReceiver
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class SysEventsReceiver : HiltBroadcastReceiver() {

    @Inject
    internal lateinit var alarmRepo: AlarmRepo

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.isValidIntent()) {
            alarmRepo.updateAlarms(DeadlineNotificationReceiver::class.java)
        }
    }

    private fun Intent.isValidIntent(): Boolean {
        return action == Intent.ACTION_BOOT_COMPLETED && action == Intent.ACTION_LOCALE_CHANGED
    }
}
