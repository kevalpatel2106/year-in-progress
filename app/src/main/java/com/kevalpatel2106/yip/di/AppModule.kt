package com.kevalpatel2106.yip.di

import android.appwidget.AppWidgetManager
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.kevalpatel2106.yip.notifications.DeadlineNotificationHandler
import com.kevalpatel2106.yip.notifications.DeadlineNotificationHandlerImpl
import com.kevalpatel2106.yip.utils.AppLaunchIntentProvider
import com.kevalpatel2106.yip.utils.AppLaunchIntentProviderImpl
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import com.kevalpatel2106.yip.utils.AppShortcutHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
internal class AppModule {

    @Singleton
    @Provides
    fun provideAppLaunchHelper(): AppLaunchIntentProvider = AppLaunchIntentProviderImpl()

    @Singleton
    @Provides
    fun provideAppShortcutHelper(
        @ApplicationContext application: Context,
        appLaunchIntentProvider: AppLaunchIntentProvider
    ): AppShortcutHelper {
        return AppShortcutHelperImpl(application, appLaunchIntentProvider)
    }

    @Singleton
    @Provides
    fun provideDeadlineNotificationHandler(
        @ApplicationContext application: Context,
        appLaunchIntentProvider: AppLaunchIntentProvider
    ): DeadlineNotificationHandler {
        return DeadlineNotificationHandlerImpl(application, appLaunchIntentProvider)
    }

    @Singleton
    @Provides
    fun appUpdateManager(@ApplicationContext context: Context): AppUpdateManager {
        return AppUpdateManagerFactory.create(context)
    }

    @Singleton
    @Provides
    fun appReviewManager(@ApplicationContext context: Context): ReviewManager {
        return ReviewManagerFactory.create(context)
    }

    @Singleton
    @Provides
    fun provideAppWidgetService(@ApplicationContext application: Context): AppWidgetManager =
        application.getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager
}
