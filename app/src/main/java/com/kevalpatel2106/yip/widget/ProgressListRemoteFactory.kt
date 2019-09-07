package com.kevalpatel2106.yip.widget

import android.app.Application
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import timber.log.Timber
import javax.inject.Inject

internal class ProgressListRemoteFactory @Inject constructor(
    private val application: Application,
    private val yipRepo: YipRepo
) : RemoteViewsService.RemoteViewsFactory {

    private val progresses: ArrayList<Progress> = arrayListOf()

    override fun getViewAt(position: Int): RemoteViews? {
        if (progresses.size - 1 < position) return null
        val rowView = RemoteViews(application.packageName, R.layout.row_widget_progress_list)
        with(progresses[position]) {

            rowView.setTextViewText(R.id.widget_battery_list_name_tv, this.title)

            @Suppress("DEPRECATION")
            rowView.setTextViewText(
                R.id.widget_battery_list_level_tv,
                application.getString(R.string.progress_percentage, this.percent)
            )

            rowView.setTextColor(
                R.id.widget_battery_list_level_tv,
                this.color.colorInt
            )

            rowView.setOnClickFillInIntent(
                R.id.battery_list_row,
                AppLaunchHelper.launchWithProgressDetail(application, this.id)
            )
        }
        return rowView
    }

    override fun onDataSetChanged() {
        val devices = yipRepo.observeAllProgress()
            .doOnError { Timber.e(it) }
            .firstElement()
            .blockingGet()
        progresses.clear()
        progresses.addAll(devices)
    }

    override fun onCreate() = Unit
    override fun getLoadingView(): RemoteViews? = null
    override fun getItemId(position: Int): Long = progresses[position].id
    override fun hasStableIds(): Boolean = true
    override fun getCount(): Int = progresses.size
    override fun getViewTypeCount(): Int = 1
    override fun onDestroy() = Unit
}
