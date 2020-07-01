@file:Suppress("TooManyFunctions")

package com.kevalpatel2106.yip.di

import android.app.AlarmManager
import android.appwidget.AppWidgetManager
import com.kevalpatel2106.yip.YIPApplication
import com.kevalpatel2106.yip.dashboard.DashboardActivity
import com.kevalpatel2106.yip.detail.DetailFragment
import com.kevalpatel2106.yip.edit.EditProgressActivity
import com.kevalpatel2106.yip.payment.PaymentActivity
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.progressesRepo.ProgressRepo
import com.kevalpatel2106.yip.repo.utils.dateFormatter.DateFormatter
import com.kevalpatel2106.yip.repo.utils.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.utils.validator.Validator
import com.kevalpatel2106.yip.settings.SettingsActivity
import com.kevalpatel2106.yip.settings.preferenceList.SettingsFragment
import com.kevalpatel2106.yip.splash.SplashActivity
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import com.kevalpatel2106.yip.webviews.WebViewActivity
import com.kevalpatel2106.yip.widget.ProgressListRemoteService
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
    fun getProgressRepo(): ProgressRepo
    fun getAlarmRepo(): AlarmRepo
    fun getDateFormatter(): DateFormatter
    fun getValidator(): Validator
    fun getAppShortcutHelper(): AppShortcutHelper

    fun inject(yipApplication: YIPApplication)

    fun inject(splashActivity: SplashActivity)
    fun inject(dashboardActivity: DashboardActivity)
    fun inject(editProgressActivity: EditProgressActivity)
    fun inject(paymentActivity: PaymentActivity)
    fun inject(webViewActivity: WebViewActivity)

    fun inject(detailFragment: DetailFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(settingsActivity: SettingsActivity)

    fun inject(progressListRemoteService: ProgressListRemoteService)
}
