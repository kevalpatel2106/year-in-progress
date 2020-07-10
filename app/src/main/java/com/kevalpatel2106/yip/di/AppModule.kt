package com.kevalpatel2106.yip.di

import android.appwidget.AppWidgetManager
import android.content.Context
import com.kevalpatel2106.yip.notifications.DeadlineNotificationHandler
import com.kevalpatel2106.yip.notifications.DeadlineNotificationHandlerImpl
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import com.kevalpatel2106.yip.utils.AppLaunchHelperImpl
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
    fun provideAppLaunchHelper(): AppLaunchHelper = AppLaunchHelperImpl()

    @Singleton
    @Provides
    fun provideAppShortcutHelper(
        @ApplicationContext application: Context,
        appLaunchHelper: AppLaunchHelper
    ): AppShortcutHelper {
        return AppShortcutHelperImpl(application, appLaunchHelper)
    }

    @Singleton
    @Provides
    fun provideDeadlineNotificationHandler(
        @ApplicationContext application: Context,
        appLaunchHelper: AppLaunchHelper
    ): DeadlineNotificationHandler {
        return DeadlineNotificationHandlerImpl(application, appLaunchHelper)
    }
    @Singleton
    @Provides
    fun provideAppWidgetService(@ApplicationContext application: Context): AppWidgetManager =
        application.getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager
}
