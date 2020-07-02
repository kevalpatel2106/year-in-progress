package com.kevalpatel2106.yip.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
internal class DeadlineNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var deadlineRepo: DeadlineRepo

    override fun onReceive(context: Context?, intent: Intent?) {
        val deadlineId = intent?.getLongExtra(AlarmRepo.ARG_DEADLINE_ID, -1L) ?: -1
        context?.let { ctx ->
            deadlineRepo.observeDeadline(deadlineId)
                .firstOrError()
                .filter { deadline ->
                    return@filter deadline.notificationPercent.findLast {
                        it in deadline.percent - 1..deadline.percent + 1
                    } != null
                }
                .subscribe({ deadline ->
                    DeadlineNotification.notify(ctx, deadline)
                }, {
                    Timber.e(it)
                })
        }
    }
}
