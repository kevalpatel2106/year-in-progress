package com.kevalpatel2106.yip.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kevalpatel2106.yip.core.di.provideViewModel
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import javax.inject.Inject

internal class SplashActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private val model: SplashViewModel by lazy {
        provideViewModel(viewModelProvider, SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this@SplashActivity)
        model.refreshPurchaseState(this@SplashActivity)
        model.signInAsAnonymousUser()
        AppLaunchHelper.launchFlow(this@SplashActivity, intent)
    }
}
