package com.kevalpatel2106.yip.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import androidx.navigation.navArgs
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.dashboard.DashboardActivityArgs
import com.kevalpatel2106.yip.edit.EditDeadlineActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val model: SplashViewModel by viewModels()
    private val args by navArgs<SplashActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.init()
        openNextDestination()
    }

    private fun openNextDestination() {
        when (intent.action) {
            getString(R.string.launch_action_create_new_deadline) -> {
                TaskStackBuilder.create(this)
                    .addNextIntent(
                        DashboardActivity.launchIntent(this, DashboardActivityArgs())
                    )
                    .addNextIntent(EditDeadlineActivity.createNewIntent(this))
                    .startActivities()
            }
            getString(R.string.launch_action_open_deadline) -> {
                DashboardActivity.launch(this, DashboardActivityArgs(args.deadlineId))
            }
            else -> DashboardActivity.launch(this, DashboardActivityArgs())
        }
        finish()
    }
}
