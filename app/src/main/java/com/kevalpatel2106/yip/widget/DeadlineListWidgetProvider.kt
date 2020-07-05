package com.kevalpatel2106.yip.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeadlineListWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var sharedPrefsProvider: SharedPrefsProvider

    @Inject
    lateinit var appLaunchHelper: AppLaunchHelper

    @Inject
    lateinit var appWidgetManager: AppWidgetManager

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        // Update the list only
        sharedPrefsProvider.getStringFromPreference(WIDGET_IDS)?.split(",")?.map {
            it.toInt()
        }?.toIntArray()?.let { widgetIds ->
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
                    appLaunchHelper.getAppLaunchIntent(context),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            // Click intent for title
            remoteViews.setOnClickPendingIntent(
                R.id.widget_title,
                PendingIntent.getActivity(
                    context,
                    0,
                    appLaunchHelper.getAppLaunchIntent(context),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        sharedPrefsProvider.savePreferences(WIDGET_IDS, appWidgetIds?.joinToString(","))
    }

    companion object {
        private const val WIDGET_IDS = "widget_ids"
    }
}
