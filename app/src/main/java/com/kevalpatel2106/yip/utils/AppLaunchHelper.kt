package com.kevalpatel2106.yip.utils

import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.edit.EditProgressActivity
import com.kevalpatel2106.yip.splash.SplashActivity

internal object AppLaunchHelper {
    private const val ACTION_CREATE_PROGRESS = "com.kevalpatel2106.yip.create_new"
    private const val ACTION_LAUNCH_WITH_PROGRESS = "com.kevalpatel2106.yip.open_progress"
    private const val ARG_PROGRESS_ID = "progress_id_to_launch"

    internal fun launchWithProgressDetail(
        context: Context,
        progressIdToLaunch: Long
    ): Intent {
        return Intent(context, SplashActivity::class.java).apply {
            action = ACTION_LAUNCH_WITH_PROGRESS
            putExtra(ARG_PROGRESS_ID, progressIdToLaunch)
            addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_NEW_TASK
            )
        }
    }

    internal fun launchIntent(context: Context) =
        context.prepareLaunchIntent(SplashActivity::class.java, true)

    internal fun launchFlow(splashActivity: SplashActivity, intent: Intent) {
        when {
            intent.action == ACTION_CREATE_PROGRESS -> {
                TaskStackBuilder.create(splashActivity)
                    .addNextIntent(DashboardActivity.launchIntent(splashActivity))
                    .addNextIntent(EditProgressActivity.createNewDeadlineIntent(splashActivity))
                    .startActivities()
            }
            intent.action == ACTION_LAUNCH_WITH_PROGRESS -> {
                DashboardActivity.launch(
                    splashActivity,
                    intent.getLongExtra(ARG_PROGRESS_ID, -1)
                )
            }
            else -> DashboardActivity.launch(splashActivity)
        }
    }
}
