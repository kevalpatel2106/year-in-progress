package com.kevalpatel2106.yip.di

import com.kevalpatel2106.yip.dashboard.inAppUpdate.InAppUpdateHelper
import com.kevalpatel2106.yip.dashboard.inAppUpdate.InAppUpdateHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
internal abstract class AppBindings {

    @Binds
    abstract fun bindInAppUpdateHelper(helperImpl: InAppUpdateHelperImpl): InAppUpdateHelper
}
