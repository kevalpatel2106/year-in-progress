package com.kevalpatel2106.yip.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.repo.providers.AlarmProvider
import com.kevalpatel2106.yip.repo.providers.NtpProvider
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

internal class ProgressNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var yipRepo: YipRepo

    @Inject
    lateinit var ntpProvider: NtpProvider

    override fun onReceive(context: Context?, intent: Intent?) {
        val progressId = intent?.getLongExtra(AlarmProvider.ARG_PROGRESS_ID, -1L) ?: -1
        AndroidInjection.inject(this, context)

        context?.let { ctx ->
            yipRepo.observeProgress(progressId)
                    .firstOrError()
                    .filter { progress ->
                        val triggerPercent = calculateTriggerPercent(
                                progress.start.time,
                                progress.end.time,
                                ntpProvider.now().time
                        )
                        return@filter progress.notificationPercent.findLast { it in triggerPercent - 1..triggerPercent + 1 } != null
                    }
                    .subscribe({
                        ProgressNotification.notify(ctx, it)
                    }, {
                        Timber.e(it)
                    })
        }
    }

    companion object {
        private fun calculateTriggerPercent(startMills: Long, endMills: Long, triggerMills: Long): Float {
            return (((triggerMills - startMills) * 100) / (endMills - startMills)).toFloat()
        }
    }
}