package com.kevalpatel2106.yip.repo.di

import android.app.Application
import android.preference.PreferenceManager
import com.kevalpatel2106.yip.repo.utils.NtpProvider
import com.kevalpatel2106.yip.repo.utils.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.utils.db.YipDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideSharedPrefs(): SharedPrefsProvider {
        return SharedPrefsProvider(PreferenceManager.getDefaultSharedPreferences(application))
    }

    @Singleton
    @Provides
    fun provideDatabase(): YipDatabase {
        return YipDatabase.create(application)
    }

    @Singleton
    @Provides
    fun provideNtp(): NtpProvider {
        return NtpProvider(application)
    }
}