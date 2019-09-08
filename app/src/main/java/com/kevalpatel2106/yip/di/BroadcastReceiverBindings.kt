package com.kevalpatel2106.yip.di

import com.kevalpatel2106.yip.notifications.ProgressNotificationReceiver
import com.kevalpatel2106.yip.utils.SysEventsReceiver
import com.kevalpatel2106.yip.widget.ProgressListWidgetProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class BroadcastReceiverBindings {

    @ContributesAndroidInjector
    internal abstract fun contributesProgressListWidgetProvider(): ProgressListWidgetProvider

    @ContributesAndroidInjector
    internal abstract fun contributesBootCompleteReceiver(): SysEventsReceiver

    @ContributesAndroidInjector
    internal abstract fun contributesProgressNotificationReciver(): ProgressNotificationReceiver
}
