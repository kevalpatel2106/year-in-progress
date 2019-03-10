@file:Suppress("TooManyFunctions")

package com.kevalpatel2106.yip.core.di

import android.app.AlarmManager
import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Kevalpatel2106 on 17-Apr-18.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
@Module
internal class CoreModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideApplication(): Application {
        return application
    }

    @Singleton
    @Provides
    fun provideAlarmManager(): AlarmManager {
        return application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Singleton
    @Provides
    fun provideAppWidgetService(): AppWidgetManager {
        return application.getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager
    }
}
