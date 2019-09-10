@file:Suppress("TooManyFunctions")

package com.kevalpatel2106.yip.core.di

import android.app.AlarmManager
import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.Context
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.di.RepositoryModule
import com.kevalpatel2106.yip.repo.progressesRepo.ProgressRepo
import com.kevalpatel2106.yip.repo.providers.SharedPrefsProvider
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Kevalpatel2106 on 17-Apr-18.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
@Singleton
@Component(modules = [CoreModule::class, RepositoryModule::class])
interface CoreComponent {

    fun getContext(): Context

    fun getApplication(): Application

    fun getAlarmManager(): AlarmManager

    fun getAppWidgetService(): AppWidgetManager

    fun getDatabase(): YipDatabase

    fun getSharedPrefs(): SharedPrefsProvider

    fun getBillingRepo(): BillingRepo
    fun getProgressRepo(): ProgressRepo
    fun getAlarmRepo(): AlarmRepo

    companion object {
        fun build(application: Application): CoreComponent {
            return DaggerCoreComponent.builder()
                .coreModule(CoreModule(application))
                .repositoryModule(RepositoryModule(application))
                .build()
        }
    }
}
