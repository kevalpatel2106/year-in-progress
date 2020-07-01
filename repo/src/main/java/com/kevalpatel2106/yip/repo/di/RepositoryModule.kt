package com.kevalpatel2106.yip.repo.di

import android.app.AlarmManager
import android.content.Context
import androidx.preference.PreferenceManager
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepoImpl
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepoImpl
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.progressesRepo.ProgressRepo
import com.kevalpatel2106.yip.repo.progressesRepo.ProgressRepoImpl
import com.kevalpatel2106.yip.repo.utils.TimeProvider
import com.kevalpatel2106.yip.repo.utils.dateFormatter.DateFormatter
import com.kevalpatel2106.yip.repo.utils.dateFormatter.DateFormatterImpl
import com.kevalpatel2106.yip.repo.utils.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.utils.sharedPrefs.SharedPrefsProviderImpl
import com.kevalpatel2106.yip.repo.utils.validator.Validator
import com.kevalpatel2106.yip.repo.utils.validator.ValidatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    internal fun provideAlarmRepo(
        @ApplicationContext application: Context,
        alarmManager: AlarmManager,
        yipDatabase: YipDatabase
    ): AlarmRepo = AlarmRepoImpl(alarmManager, application, TimeProvider, yipDatabase)

    @Singleton
    @Provides
    fun provideBillingRepo(
        @ApplicationContext application: Context,
        sharedPrefsProvider: SharedPrefsProvider
    ): BillingRepo = BillingRepoImpl(application, sharedPrefsProvider)

    @Singleton
    @Provides
    internal fun provideProgressRepo(
        @ApplicationContext application: Context,
        db: YipDatabase,
        sharedPrefsProvider: SharedPrefsProvider
    ): ProgressRepo = ProgressRepoImpl(application, db, TimeProvider, sharedPrefsProvider)

    @Singleton
    @Provides
    fun provideValidator(@ApplicationContext application: Context): Validator =
        ValidatorImpl(application)

    @Singleton
    @Provides
    fun provideDateFormatter(
        @ApplicationContext application: Context,
        sharedPrefsProvider: SharedPrefsProvider
    ): DateFormatter =
        DateFormatterImpl(
            application,
            sharedPrefsProvider
        )

    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext application: Context): SharedPrefsProvider =
        SharedPrefsProviderImpl(
            PreferenceManager.getDefaultSharedPreferences(application)
        )

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext application: Context): YipDatabase =
        YipDatabase.create(application)
}
