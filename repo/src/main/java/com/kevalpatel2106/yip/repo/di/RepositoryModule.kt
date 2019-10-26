package com.kevalpatel2106.yip.repo.di

import android.app.AlarmManager
import android.app.Application
import androidx.preference.PreferenceManager
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepo
import com.kevalpatel2106.yip.repo.alarmRepo.AlarmRepoImpl
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepoImpl
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.progressesRepo.ProgressRepo
import com.kevalpatel2106.yip.repo.progressesRepo.ProgressRepoImpl
import com.kevalpatel2106.yip.repo.utils.DateFormatter
import com.kevalpatel2106.yip.repo.utils.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.utils.SharedPrefsProviderImpl
import com.kevalpatel2106.yip.repo.utils.TimeProvider
import com.kevalpatel2106.yip.repo.utils.Validator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val application: Application) {

    @Singleton
    @Provides
    internal fun provideAlarmRepo(
        alarmManager: AlarmManager,
        application: Application,
        yipDatabase: YipDatabase
    ): AlarmRepo {
        return AlarmRepoImpl(alarmManager, application, TimeProvider, yipDatabase)
    }

    @Singleton
    @Provides
    fun provideBillingRepo(
        application: Application,
        sharedPrefsProvider: SharedPrefsProvider
    ): BillingRepo {
        return BillingRepoImpl(application, sharedPrefsProvider)
    }

    @Singleton
    @Provides
    internal fun provideProgressRepo(
        application: Application,
        db: YipDatabase,
        sharedPrefsProvider: SharedPrefsProvider
    ): ProgressRepo {
        return ProgressRepoImpl(application, db, TimeProvider, sharedPrefsProvider)
    }

    @Singleton
    @Provides
    fun provideValidator(application: Application): Validator {
        return Validator(application)
    }

    @Singleton
    @Provides
    fun provideDateFormatter(
        application: Application,
        sharedPrefsProvider: SharedPrefsProvider
    ): DateFormatter {
        return DateFormatter(application, sharedPrefsProvider)
    }

    @Singleton
    @Provides
    fun provideSharedPrefs(): SharedPrefsProvider {
        return SharedPrefsProviderImpl(PreferenceManager.getDefaultSharedPreferences(application))
    }

    @Singleton
    @Provides
    fun provideDatabase(): YipDatabase = YipDatabase.create(application)
}
