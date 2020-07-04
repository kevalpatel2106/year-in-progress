package com.kevalpatel2106.yip.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val model: SplashViewModel by viewModels()

    @Inject
    lateinit var appLaunchHelper: AppLaunchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.refreshPurchase()
        appLaunchHelper.launchAppFromSplashScreen(this@SplashActivity, intent)
    }
}
