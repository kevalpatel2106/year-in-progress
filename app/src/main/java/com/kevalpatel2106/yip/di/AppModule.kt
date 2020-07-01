package com.kevalpatel2106.yip.di

import android.app.AlarmManager
import android.appwidget.AppWidgetManager
import android.content.Context
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
    internal fun provideAppShortcutHelper(@ApplicationContext application: Context): AppShortcutHelper {
        return AppShortcutHelperImpl(application)
    }

    @Singleton
    @Provides
    fun provideAlarmManager(@ApplicationContext application: Context): AlarmManager =
        application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Singleton
    @Provides
    fun provideAppWidgetService(@ApplicationContext application: Context): AppWidgetManager =
        application.getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager
}
