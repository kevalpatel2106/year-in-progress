package com.kevalpatel2106.yip.splash

import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.edit.EditProgressActivity

internal object AppLaunchHelper {
    internal const val ACTION_CREATE_PROGRESS = "com.kevalpatel2106.yip.create_new"
    internal const val ACTION_LAUNCH_WITH_PROGRESS = "com.kevalpatel2106.yip.open_progress"
    internal const val ARG_PROGRESS_ID = "progress_id_to_launch"

    internal fun launchWithProgressDetail(
        context: Context,
        progressIdToLaunch: Long
    ): Intent {
        return Intent(context, SplashActivity::class.java).apply {
            action = ACTION_LAUNCH_WITH_PROGRESS
            putExtra(ARG_PROGRESS_ID, progressIdToLaunch)
        }
    }

    internal fun launchFlow(splashActivity: SplashActivity, intent: Intent) {
        when {
            intent.action == AppLaunchHelper.ACTION_CREATE_PROGRESS -> {
                TaskStackBuilder.create(splashActivity)
                    .addNextIntent(DashboardActivity.launchIntent(splashActivity))
                    .addNextIntent(EditProgressActivity.createNewDeadlineIntent(splashActivity))
                    .startActivities()
            }
            intent.action == AppLaunchHelper.ACTION_LAUNCH_WITH_PROGRESS -> {
                DashboardActivity.launch(
                    splashActivity,
                    intent.getLongExtra(AppLaunchHelper.ARG_PROGRESS_ID, -1)
                )
            }
            else -> DashboardActivity.launch(splashActivity)
        }
    }
}