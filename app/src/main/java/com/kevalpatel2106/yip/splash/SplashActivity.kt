package com.kevalpatel2106.yip.splash

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kevalpatel2106.yip.core.prepareLaunchIntent
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.di.getAppComponent
import javax.inject.Inject

internal class SplashActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var model: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this@SplashActivity)
        model = ViewModelProviders
            .of(this@SplashActivity, viewModelProvider)
            .get(SplashViewModel::class.java)

        // Perform anonymous auth
        model.signInAsAnonymousUser()

        // open dashboard
        DashboardActivity.launch(this@SplashActivity)
    }

    companion object {
        fun launchIntent(context: Context) = context.prepareLaunchIntent(SplashActivity::class.java)
    }
}