package com.kevalpatel2106.yip.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import javax.inject.Inject

internal class SplashActivity : AppCompatActivity() {
    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private val model: SplashViewModel by viewModels { viewModelProvider }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this@SplashActivity)
        model.refreshPurchase()
        AppLaunchHelper.launchFlow(this@SplashActivity, intent)
    }
}
