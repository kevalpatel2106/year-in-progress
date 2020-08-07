package com.kevalpatel2106.yip.di

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.kevalpatel2106.yip.dashboard.inAppUpdate.InAppUpdateHelper
import com.kevalpatel2106.yip.dashboard.inAppUpdate.InAppUpdateManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
internal class ActivityModule {

    @ActivityScoped
    @Provides
    fun provideAppUpdateHelper(activity: Activity, helper: InAppUpdateHelper): InAppUpdateManager {
        val appUpdateManager = AppUpdateManagerFactory.create(activity)
        return InAppUpdateManager(appUpdateManager, helper, activity)
    }
}
