package com.kevalpatel2106.yip.widget

import android.app.Application
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.repo.utils.NtpProvider
import javax.inject.Inject

internal class ProgressListRemoteFactory(private val application: Application) : RemoteViewsService.RemoteViewsFactory {
    private val progresses: ArrayList<Progress> = arrayListOf()

    @Inject
    internal lateinit var yipRepo: YipRepo

    @Inject
    internal lateinit var ntpProvider: NtpProvider

    override fun getViewAt(position: Int): RemoteViews {
        val rowView = RemoteViews(application.packageName, R.layout.row_widget_progress_list)
        with(progresses[position]) {
            rowView.setTextViewText(R.id.widget_battery_list_name_tv, this.title)
            rowView.setTextViewText(
                    R.id.widget_battery_list_level_tv,
                    application.getString(R.string.progress_percentage, this.percent(ntpProvider.now()))
            )
            rowView.setTextColor(
                    R.id.widget_battery_list_level_tv,
                    this.color.value
            )
        }
        return rowView
    }

    override fun onCreate() {
        application.getAppComponent().inject(this@ProgressListRemoteFactory)
    }

    override fun onDataSetChanged() {
        val devices = yipRepo.observeAllProgress()
                .firstElement()
                .blockingGet()
        progresses.clear()
        progresses.addAll(devices)
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getItemId(position: Int): Long = progresses[position].id
    override fun hasStableIds(): Boolean = true
    override fun getCount(): Int = progresses.size
    override fun getViewTypeCount(): Int = 1
    override fun onDestroy() = Unit
}