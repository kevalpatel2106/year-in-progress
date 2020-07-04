package com.kevalpatel2106.yip.utils

import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.splash.SplashActivity

interface AppLaunchHelper {
    fun getLaunchIntentWithDeadlineDetail(context: Context, idToLaunch: Long): Intent
    fun getAppLaunchIntent(context: Context): Intent
    fun launchAppFromSplashScreen(splashActivity: SplashActivity, intent: Intent)
}
