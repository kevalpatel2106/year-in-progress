package com.kevalpatel2106.yip.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.WidgetConfigTheme
import com.kevalpatel2106.yip.repo.widgetConfig.WidgetConfigRepo
import com.kevalpatel2106.yip.utils.AppLaunchIntentProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeadlineListWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var widgetConfigRepo: WidgetConfigRepo

    @Inject
    lateinit var appLaunchIntentProvider: AppLaunchIntentProvider

    @Inject
    lateinit var appWidgetManager: AppWidgetManager

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        // Update the list only
        widgetConfigRepo.getWidgetIds()?.let { widgetIds ->
            appWidgetManager.notifyAppWidgetViewDataChanged(
                widgetIds,
                R.id.widget_devices_list
            )
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray?
    ) {
        appWidgetIds?.forEach { widgetId ->
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_deadline_list)
            val widgetConfig = widgetConfigRepo.getWidgetConfig(widgetId)

            // set background
            val backgroundRes = if (widgetConfig.theme == WidgetConfigTheme.DARK) {
                R.drawable.bg_widget_dark
            } else {
                R.drawable.bg_widget_white
            }
            remoteViews.setImageViewResource(R.id.widget_background_image, backgroundRes)

            // set list
            remoteViews.setRemoteAdapter(
                R.id.widget_devices_list,
                Intent(context, DeadlineListRemoteService::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                }
            )
            remoteViews.setPendingIntentTemplate(
                R.id.widget_devices_list,
                PendingIntent.getActivity(
                    context,
                    0,
                    appLaunchIntentProvider.launchAppWithDeadlineListIntent(context),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            // Click intent for title
            remoteViews.setOnClickPendingIntent(
                R.id.widget_title,
                PendingIntent.getActivity(
                    context,
                    0,
                    appLaunchIntentProvider.launchAppWithDeadlineListIntent(context),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        widgetConfigRepo.saveWidgetIds(appWidgetIds)
    }
}
