package com.kevalpatel2106.yip.utils

import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.splash.SplashActivity

interface AppLaunchHelper {
    fun launchAppWithDeadlineDetail(context: Context, idToLaunch: Long): Intent
    fun launchAppWithDeadlineList(context: Context): Intent
    fun launchAppFromSplashScreen(splashActivity: SplashActivity, intent: Intent)
}
