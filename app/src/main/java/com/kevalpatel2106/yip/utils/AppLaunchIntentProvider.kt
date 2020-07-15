package com.kevalpatel2106.yip.utils

import android.content.Context
import android.content.Intent

interface AppLaunchIntentProvider {
    fun launchAppWithDeadlineDetailIntent(context: Context, idToLaunch: Long): Intent
    fun launchAppWithDeadlineListIntent(context: Context): Intent
}
