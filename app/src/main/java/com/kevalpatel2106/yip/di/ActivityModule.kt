package com.kevalpatel2106.yip.di

import androidx.fragment.app.FragmentActivity
import com.google.android.play.core.appupdate.AppUpdateManager
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
    fun provideAppUpdateHelper(
        activity: FragmentActivity,
        updateManager: AppUpdateManager
    ) = InAppUpdateManager(updateManager, activity)
}
