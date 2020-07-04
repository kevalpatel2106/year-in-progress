package com.kevalpatel2106.yip.utils

import android.content.Context
import android.content.Intent
import androidx.annotation.VisibleForTesting
import androidx.core.app.TaskStackBuilder
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.edit.EditDeadlineActivity
import com.kevalpatel2106.yip.splash.SplashActivity

internal class AppLaunchHelperImpl : AppLaunchHelper {

    override fun getLaunchIntentWithDeadlineDetail(context: Context, idToLaunch: Long): Intent {
        return Intent(context, SplashActivity::class.java).apply {
            action = ACTION_LAUNCH_WITH_DEADLINE
            putExtra(ARG_DEADLINE_ID, idToLaunch)
            addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_NEW_TASK
            )
        }
    }

    override fun getAppLaunchIntent(context: Context) =
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
                DashboardActivity.launch(
                    splashActivity,
                    intent.getLongExtra(ARG_DEADLINE_ID, -1)
                )
            }
            else -> DashboardActivity.launch(splashActivity)
        }
    }

    companion object {
        @VisibleForTesting
        internal const val ACTION_CREATE_DEADLINE = "com.kevalpatel2106.yip.create_new"

        @VisibleForTesting
        internal const val ACTION_LAUNCH_WITH_DEADLINE = "com.kevalpatel2106.yip.open_progress"

        @VisibleForTesting
        internal const val ARG_DEADLINE_ID = "deadline_id_to_launch"
    }
}
