package com.kevalpatel2106.yip.repo.di

import android.app.AlarmManager
import android.content.Context
import androidx.preference.PreferenceManager
import com.kevalpatel2106.yip.repo.db.DeadlineDao
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
internal class RepositoryModule {

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

    @Singleton
    @Provides
    fun provideDeadlineDao(db: YipDatabase): DeadlineDao {
        return db.getDeviceDao()
    }

    @Singleton
    @Provides
    fun provideAlarmManager(@ApplicationContext application: Context): AlarmManager =
        application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
}
