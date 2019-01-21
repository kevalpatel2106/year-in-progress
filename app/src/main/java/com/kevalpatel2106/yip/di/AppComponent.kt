package com.kevalpatel2106.yip.di

import android.content.Context
import com.kevalpatel2106.yip.YIPApplication
import com.kevalpatel2106.yip.core.di.CoreComponent
import com.kevalpatel2106.yip.core.di.SessionScope
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.detail.DetailFragment
import com.kevalpatel2106.yip.edit.EditProgressActivity
import com.kevalpatel2106.yip.payment.PaymentActivity
import com.kevalpatel2106.yip.settings.SettingsFragment
import com.kevalpatel2106.yip.splash.SplashActivity
import com.kevalpatel2106.yip.widget.ProgressListRemoteFactory
import com.kevalpatel2106.yip.widget.ProgressListWidgetProvider
import dagger.Component

@SessionScope
@Component(dependencies = [CoreComponent::class], modules = [ViewModelBindings::class])
internal interface AppComponent {
    fun inject(dashboardActivity: DashboardActivity)
    fun inject(detailFragment: DetailFragment)
    fun inject(editProgressActivity: EditProgressActivity)
    fun inject(paymentActivity: PaymentActivity)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(progressListRemoteFactory: ProgressListRemoteFactory)
    fun inject(progressListWidgetProvider: ProgressListWidgetProvider)
    fun inject(splashActivity: SplashActivity)
}

internal fun Context.getAppComponent(): AppComponent {
    return DaggerAppComponent.builder()
        .coreComponent((applicationContext as YIPApplication).coreComponent)
        .build()
}