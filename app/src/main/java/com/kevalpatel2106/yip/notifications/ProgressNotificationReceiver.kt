package com.kevalpatel2106.yip.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.repo.providers.AlarmProvider
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

internal class ProgressNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var yipRepo: YipRepo

    override fun onReceive(context: Context?, intent: Intent?) {
        val progressId = intent?.getLongExtra(AlarmProvider.ARG_PROGRESS_ID, -1L) ?: -1
        AndroidInjection.inject(this, context)

        context?.let { ctx ->
            yipRepo.observeProgress(progressId)
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