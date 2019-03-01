package com.kevalpatel2106.yip.di

import android.content.Context
import com.kevalpatel2106.yip.YIPApplication
import com.kevalpatel2106.yip.core.di.CoreComponent
import com.kevalpatel2106.yip.core.di.SessionScope
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.detail.DetailFragment
import com.kevalpatel2106.yip.edit.EditProgressActivity
import com.kevalpatel2106.yip.payment.PaymentActivity
import com.kevalpatel2106.yip.settings.SettingsActivity
import com.kevalpatel2106.yip.settings.SettingsFragment
import com.kevalpatel2106.yip.splash.SplashActivity
import com.kevalpatel2106.yip.utils.NotificationViewer
import com.kevalpatel2106.yip.webviews.WebViewActivity
import com.kevalpatel2106.yip.widget.ProgressListRemoteService
import dagger.Component
import dagger.android.AndroidInjectionModule

@SessionScope
@Component(
        dependencies = [CoreComponent::class],
        modules = [AndroidInjectionModule::class, ViewModelBindings::class, BroadcastReceiverBindings::class]
)
internal interface AppComponent {
    fun inject(yipApplication: YIPApplication)

    fun inject(splashActivity: SplashActivity)
    fun inject(dashboardActivity: DashboardActivity)
    fun inject(editProgressActivity: EditProgressActivity)
    fun inject(paymentActivity: PaymentActivity)
    fun inject(webViewActivity: WebViewActivity)

    fun inject(detailFragment: DetailFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(settingsActivity: SettingsActivity)

    fun inject(notificationViewer: NotificationViewer)

    fun inject(progressListRemoteService: ProgressListRemoteService)
}

internal fun Context.getAppComponent(): AppComponent {
    return DaggerAppComponent.builder()
            .coreComponent((applicationContext as YIPApplication).coreComponent)
            .build()
}