package com.kevalpatel2106.yip.di

import android.app.Application
import com.kevalpatel2106.yip.core.di.SessionScope
import com.kevalpatel2106.yip.utils.AppShortcutHelper
import com.kevalpatel2106.yip.utils.AppShortcutHelperImpl
import dagger.Module
import dagger.Provides

@Module
internal class AppModule {

    @SessionScope
    @Provides
    internal fun provideAppShortcutHelper(application: Application): AppShortcutHelper {
        return AppShortcutHelperImpl(application)
    }
}
