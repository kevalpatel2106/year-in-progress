package com.kevalpatel2106.yip.utils

import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.ext.getLaunchIntent
import com.kevalpatel2106.yip.splash.SplashActivity
import com.kevalpatel2106.yip.splash.SplashActivityArgs

internal class AppLaunchIntentProviderImpl : AppLaunchIntentProvider {

    override fun launchAppWithDeadlineDetailIntent(context: Context, idToLaunch: Long): Intent {
        return context.getLaunchIntent(SplashActivity::class.java, true) {
            action = context.getString(R.string.launch_action_open_deadline)
            putExtras(SplashActivityArgs(idToLaunch).toBundle())
        }
    }

    override fun launchAppWithDeadlineListIntent(context: Context): Intent =
        context.getLaunchIntent(SplashActivity::class.java, true)
}
