package com.kevalpatel2106.yip.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.RemoteViewsService
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.repo.widgetConfig.WidgetConfigRepo
import com.kevalpatel2106.yip.utils.AppLaunchIntentProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeadlineListRemoteService : RemoteViewsService() {

    @Inject
    internal lateinit var widgetConfigRepo: WidgetConfigRepo

    @Inject
    internal lateinit var launchIntentProvider: AppLaunchIntentProvider

    @Inject
    internal lateinit var deadlineRepo: DeadlineRepo

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        val widgetId = intent?.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: throw IllegalStateException("Widget id cannot be null.")
        return DeadlineListRemoteFactory(
            application = application,
            appLaunchIntentProvider = launchIntentProvider,
            deadlineRepo = deadlineRepo,
            widgetConfig = widgetConfigRepo.getWidgetConfig(widgetId)
        )
    }
}
