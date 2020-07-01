package com.kevalpatel2106.yip.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class SplashActivity : AppCompatActivity() {

    private val model: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.refreshPurchase()
        AppLaunchHelper.launchFlow(this@SplashActivity, intent)
    }
}
