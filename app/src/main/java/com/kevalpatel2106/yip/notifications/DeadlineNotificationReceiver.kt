package com.kevalpatel2106.yip.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.core.AppConstants.INVALID_DEADLINE_ID
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class DeadlineNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var helper: DeadlineNotificationReceiverHelper

    override fun onReceive(context: Context, intent: Intent?) =
        helper.onReceive(parseDeadlineId(intent))

    private fun parseDeadlineId(intent: Intent?): Long {
        return intent?.getLongExtra(AlarmRepo.ARG_DEADLINE_ID, INVALID_DEADLINE_ID)
            ?: INVALID_DEADLINE_ID
    }
}
