package com.kevalpatel2106.yip.utils

import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import com.kevalpatel2106.yip.core.AppConstants.INVALID_DEADLINE_ID
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.edit.EditDeadlineActivity
import com.kevalpatel2106.yip.splash.SplashActivity

internal class AppLaunchHelperImpl : AppLaunchHelper {

    override fun launchAppWithDeadlineDetail(context: Context, idToLaunch: Long): Intent {
        return context.prepareLaunchIntent(SplashActivity::class.java, true).apply {
            action = ACTION_LAUNCH_WITH_DEADLINE
            putExtra(ARG_DEADLINE_ID, idToLaunch)
        }
    }

    override fun launchAppWithDeadlineList(context: Context) =
        context.prepareLaunchIntent(SplashActivity::class.java, true)

    override fun launchAppFromSplashScreen(splashActivity: SplashActivity, intent: Intent) {
        when (intent.action) {
            ACTION_CREATE_DEADLINE -> {
                TaskStackBuilder.create(splashActivity)
                    .addNextIntent(DashboardActivity.launchIntent(splashActivity))
                    .addNextIntent(EditDeadlineActivity.createNewDeadlineIntent(splashActivity))
                    .startActivities()
            }
            ACTION_LAUNCH_WITH_DEADLINE -> {
                val deadlineId = intent.getLongExtra(ARG_DEADLINE_ID, INVALID_DEADLINE_ID)
                DashboardActivity.launch(splashActivity, deadlineId)
            }
            else -> DashboardActivity.launch(splashActivity)
        }
    }

    companion object {
        private const val ACTION_CREATE_DEADLINE = "com.kevalpatel2106.yip.create_new"
        private const val ACTION_LAUNCH_WITH_DEADLINE = "com.kevalpatel2106.yip.open_progress"
        private const val ARG_DEADLINE_ID = "deadline_id_to_launch"
    }
}
