package com.kevalpatel2106.yip.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

internal class DeadlineListRemoteFactory @Inject constructor(
    @ApplicationContext private val application: Context,
    private val deadlineRepo: DeadlineRepo
) : RemoteViewsService.RemoteViewsFactory {

    private val deadlines: ArrayList<Deadline> = arrayListOf()

    override fun getViewAt(position: Int): RemoteViews? {
        val deadline = deadlines.getOrNull(position) ?: return null

        val rowView = RemoteViews(application.packageName, R.layout.row_widget_deadline_list)
        with(deadline) {
            rowView.setTextViewText(R.id.widget_battery_list_name_tv, title)

            rowView.setTextViewText(
                R.id.widget_battery_list_level_tv,
                application.getString(R.string.deadline_percentage, percent)
            )

            rowView.setTextColor(R.id.widget_battery_list_level_tv, color.colorInt)

            rowView.setOnClickFillInIntent(
                R.id.battery_list_row,
                AppLaunchHelper.launchWithDeadlineDetail(application, id)
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

    override fun onCreate() = Unit
    override fun getLoadingView(): RemoteViews? = null
    override fun getItemId(position: Int): Long = deadlines.getOrNull(position)?.id ?: INVALID_ID
    override fun hasStableIds(): Boolean = true
    override fun getCount(): Int = deadlines.size
    override fun getViewTypeCount(): Int = 1
    override fun onDestroy() = Unit

    companion object {
        private const val INVALID_ID = -1L
    }
}
