@file:Suppress("TooManyFunctions")

package com.kevalpatel2106.yip.repo.di

import android.app.Application
import android.preference.PreferenceManager
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.providers.SharedPrefsProvider
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
}