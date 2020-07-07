@file:Suppress("TooManyFunctions")

package com.kevalpatel2106.yip.di

import android.app.AlarmManager
import android.appwidget.AppWidgetManager
import com.kevalpatel2106.yip.YIPApplication
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.detail.DetailFragment
import com.kevalpatel2106.yip.edit.EditDeadlineActivity
import com.kevalpatel2106.yip.notifications.DeadlineNotificationHandler
import com.kevalpatel2106.yip.payment.PaymentActivity
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatter
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.repo.nightModeRepo.NightModeRepo
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.validator.Validator
import com.kevalpatel2106.yip.settings.SettingsActivity
import com.kevalpatel2106.yip.settings.preferenceList.SettingsFragment
import com.kevalpatel2106.yip.splash.SplashActivity
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import com.kevalpatel2106.yip.webviews.WebViewActivity
import com.kevalpatel2106.yip.widget.DeadlineListRemoteService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@EntryPoint
internal interface AppComponent {
    fun getAlarmManager(): AlarmManager
    fun getAppWidgetService(): AppWidgetManager
    fun getDatabase(): YipDatabase
    fun getSharedPrefs(): SharedPrefsProvider
    fun getBillingRepo(): BillingRepo
    fun getDeadlineRepo(): DeadlineRepo
    fun getAlarmRepo(): AlarmRepo
    fun getDateFormatter(): DateFormatter
    fun getValidator(): Validator
    fun getAppShortcutHelper(): AppShortcutHelper
    fun getAppLaunchHelper(): AppLaunchHelper
    fun getDeadlineNotificationHandler(): DeadlineNotificationHandler
    fun getNightModeRepo(): NightModeRepo

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
