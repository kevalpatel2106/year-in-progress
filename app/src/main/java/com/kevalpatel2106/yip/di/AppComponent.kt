@file:Suppress("TooManyFunctions")

package com.kevalpatel2106.yip.di

import android.appwidget.AppWidgetManager
import com.kevalpatel2106.yip.YIPApplication
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.detail.DetailFragment
import com.kevalpatel2106.yip.edit.EditDeadlineActivity
import com.kevalpatel2106.yip.notifications.DeadlineNotificationHandler
import com.kevalpatel2106.yip.payment.PaymentActivity
import com.kevalpatel2106.yip.settings.SettingsActivity
import com.kevalpatel2106.yip.settings.preferenceList.SettingsFragment
import com.kevalpatel2106.yip.splash.SplashActivity
import com.kevalpatel2106.yip.utils.AppLaunchIntentProvider
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import com.kevalpatel2106.yip.webviews.WebViewActivity
import com.kevalpatel2106.yip.widget.DeadlineListRemoteService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@EntryPoint
internal interface AppComponent {
    fun getAppLaunchHelper(): AppLaunchIntentProvider
    fun getAppWidgetService(): AppWidgetManager
    fun getAppShortcutHelper(): AppShortcutHelper
    fun getDeadlineNotificationHandler(): DeadlineNotificationHandler

    fun inject(yipApplication: YIPApplication)

    fun inject(splashActivity: SplashActivity)
    fun inject(dashboardActivity: DashboardActivity)
    fun inject(editDeadlineActivity: EditDeadlineActivity)
    fun inject(paymentActivity: PaymentActivity)
    fun inject(webViewActivity: WebViewActivity)

    fun inject(detailFragment: DetailFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(settingsActivity: SettingsActivity)

    fun inject(deadlineListRemoteService: DeadlineListRemoteService)
}
