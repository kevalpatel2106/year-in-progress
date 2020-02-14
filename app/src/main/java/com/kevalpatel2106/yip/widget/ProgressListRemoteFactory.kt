package com.kevalpatel2106.yip.widget

import android.app.Application
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.repo.progressesRepo.ProgressRepo
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import timber.log.Timber
import javax.inject.Inject

internal class ProgressListRemoteFactory @Inject constructor(
    private val application: Application,
    private val progressRepo: ProgressRepo
) : RemoteViewsService.RemoteViewsFactory {

    private val progresses: ArrayList<Progress> = arrayListOf()

    override fun getViewAt(position: Int): RemoteViews? {
        val progress = progresses.getOrNull(position) ?: return null

        val rowView = RemoteViews(application.packageName, R.layout.row_widget_progress_list)
        with(progress) {
            rowView.setTextViewText(R.id.widget_battery_list_name_tv, title)

            rowView.setTextViewText(
                R.id.widget_battery_list_level_tv,
                application.getString(R.string.progress_percentage, percent)
            )

            rowView.setTextColor(R.id.widget_battery_list_level_tv, color.colorInt)

            rowView.setOnClickFillInIntent(
                R.id.battery_list_row,
                AppLaunchHelper.launchWithProgressDetail(application, id)
            )
        }
        return rowView
    }

    override fun onDataSetChanged() {
        val devices = progressRepo.observeAllProgress()
            .doOnError { Timber.e(it) }
            .firstElement()
            .blockingGet()
        progresses.clear()
        progresses.addAll(devices)
    }

    override fun onCreate() = Unit
    override fun getLoadingView(): RemoteViews? = null
    override fun getItemId(position: Int): Long = progresses.getOrNull(position)?.id ?: INVALID_ID
    override fun hasStableIds(): Boolean = true
    override fun getCount(): Int = progresses.size
    override fun getViewTypeCount(): Int = 1
    override fun onDestroy() = Unit

    companion object {
        private const val INVALID_ID = -1L
    }
}
