package com.kevalpatel2106.yip.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.progressesRepo.ProgressRepo
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
internal class ProgressNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var progressRepo: ProgressRepo

    override fun onReceive(context: Context?, intent: Intent?) {
        val progressId = intent?.getLongExtra(AlarmRepo.ARG_PROGRESS_ID, -1L) ?: -1
        context?.let { ctx ->
            progressRepo.observeProgress(progressId)
                .firstOrError()
                .filter { progress ->
                    return@filter progress.notificationPercent.findLast {
                        it in progress.percent - 1..progress.percent + 1
                    } != null
                }
                .subscribe({ progress ->
                    ProgressNotification.notify(ctx, progress)
                }, {
                    Timber.e(it)
                })
        }
    }
}
