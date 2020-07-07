package com.kevalpatel2106.yip.repo.di

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepoImpl
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepoImpl
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatter
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatterImpl
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepoImpl
import com.kevalpatel2106.yip.repo.nightModeRepo.NightModeRepo
import com.kevalpatel2106.yip.repo.nightModeRepo.NightModeRepoImpl
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProviderImpl
import com.kevalpatel2106.yip.repo.timeProvider.TimeProviderImpl
import com.kevalpatel2106.yip.repo.validator.Validator
import com.kevalpatel2106.yip.repo.validator.ValidatorImpl
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
    internal fun provideNightModeRepo(
        application: Application,
        sharedPrefsProvider: SharedPrefsProvider
    ): NightModeRepo {
        return NightModeRepoImpl(application, sharedPrefsProvider)
    }

    @Singleton
    @Provides
    internal fun provideAlarmRepo(
        @ApplicationContext application: Context,
        alarmManager: AlarmManager,
        yipDatabase: YipDatabase
    ): AlarmRepo {
        return AlarmRepoImpl(alarmManager, application, TimeProviderImpl(), yipDatabase)
    }

    @Singleton
    @Provides
    fun provideBillingRepo(
        @ApplicationContext application: Context,
        sharedPrefsProvider: SharedPrefsProvider
    ): BillingRepo {
        return BillingRepoImpl(application, sharedPrefsProvider)
    }

    @Singleton
    @Provides
    internal fun provideDeadlineRepo(
        @ApplicationContext application: Context,
        db: YipDatabase,
        sharedPrefsProvider: SharedPrefsProvider
    ): DeadlineRepo {
        return DeadlineRepoImpl(application, db, TimeProviderImpl(), sharedPrefsProvider)
    }

    @Singleton
    @Provides
    fun provideValidator(@ApplicationContext application: Context): Validator {
        return ValidatorImpl(application)
    }

    @Singleton
    @Provides
    fun provideDateFormatter(
        @ApplicationContext application: Context,
        sharedPrefsProvider: SharedPrefsProvider
    ): DateFormatter {
        return DateFormatterImpl(application, sharedPrefsProvider)
    }

    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext application: Context): SharedPrefsProvider {
        return SharedPrefsProviderImpl(PreferenceManager.getDefaultSharedPreferences(application))
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext application: Context): YipDatabase {
        return YipDatabase.create(application)
    }
}
