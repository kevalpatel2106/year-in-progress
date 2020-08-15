package com.kevalpatel2106.yip.widget

import android.app.Application
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.AppConstants
import com.kevalpatel2106.yip.core.ext.getColorCompat
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.WidgetConfig
import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigTheme
import com.kevalpatel2106.yip.entity.ext.timeLeftDHM
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.utils.AppLaunchIntentProvider
import timber.log.Timber

internal class DeadlineListRemoteFactory(
    private val application: Application,
    private val deadlineRepo: DeadlineRepo,
    private val appLaunchIntentProvider: AppLaunchIntentProvider,
    private val widgetConfig: WidgetConfig
) : RemoteViewsService.RemoteViewsFactory {
    private val deadlines: ArrayList<Deadline> = arrayListOf()

    override fun getViewAt(position: Int): RemoteViews? {
        val deadline = deadlines.getOrNull(position) ?: return null

        val rowView = RemoteViews(application.packageName, R.layout.row_widget_deadline_list)
        with(deadline) {
            val textColor = when (widgetConfig.theme) {
                WidgetConfigTheme.DARK -> application.getColorCompat(android.R.color.white)
                WidgetConfigTheme.LIGHT -> application.getColorCompat(android.R.color.black)
            }
            rowView.setTextColor(R.id.widget_deadline_name_tv, textColor)
            rowView.setTextViewText(R.id.widget_deadline_name_tv, title)

            val content = when (widgetConfig.content) {
                WidgetConfigContent.PERCENT -> application.getString(
                    R.string.deadline_percentage,
                    percent
                )
                WidgetConfigContent.TIME_LEFT -> {
                    val (days, hours, mins) = deadline.timeLeftDHM()
                    application.getString(R.string.time_left_widget, days, hours, mins)
                }
            }
            rowView.setTextViewText(R.id.widget_deadline_percent_tv, content)
            rowView.setTextColor(R.id.widget_deadline_percent_tv, color.colorInt)

            rowView.setOnClickFillInIntent(
                R.id.battery_list_row,
                appLaunchIntentProvider.launchAppWithDeadlineDetailIntent(application, id)
            )
        }
        return rowView
    }

    override fun onDataSetChanged() {
        val devices = deadlineRepo.observeAllDeadlines()
            .doOnError { Timber.e(it) }
            .firstElement()
            .blockingGet()
        deadlines.clear()
        deadlines.addAll(devices)
    }

    override fun getItemId(position: Int): Long {
        return deadlines.getOrNull(position)?.id ?: AppConstants.INVALID_DEADLINE_ID
    }

    override fun onCreate() = Unit
    override fun getLoadingView(): RemoteViews? = null
    override fun hasStableIds(): Boolean = true
    override fun getCount(): Int = deadlines.size
    override fun getViewTypeCount(): Int = 1
    override fun onDestroy() = Unit
}
