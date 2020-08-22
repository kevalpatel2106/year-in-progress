package com.kevalpatel2106.yip.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigTheme
import com.kevalpatel2106.yip.entity.ext.timeLeftDHM

object DeadlineListWidgetHelper {

    @DrawableRes
    fun getWidgetBackground(theme: WidgetConfigTheme): Int {
        return if (theme == WidgetConfigTheme.DARK) {
            R.drawable.bg_widget_dark
        } else {
            R.drawable.bg_widget_white
        }
    }

    @ColorRes
    fun getTitleColor(theme: WidgetConfigTheme): Int {
        return if (theme == WidgetConfigTheme.DARK) {
            android.R.color.white
        } else {
            R.color.colorAccent
        }
    }

    @ColorRes
    fun getListRowTextColor(theme: WidgetConfigTheme): Int {
        return if (theme == WidgetConfigTheme.DARK) {
            android.R.color.white
        } else {
            android.R.color.black
        }
    }

    fun getContent(context: Context, content: WidgetConfigContent, deadline: Deadline): String {
        return when (content) {
            WidgetConfigContent.PERCENT -> context.getString(
                R.string.deadline_percentage,
                deadline.percent
            )
            WidgetConfigContent.TIME_LEFT -> {
                val (days, hours, mins) = deadline.timeLeftDHM()
                context.getString(R.string.time_left_widget, days, hours, mins)
            }
        }
    }

    fun getWidgetServiceIntent(context: Context, widgetId: Int): Intent {
        return Intent(context, DeadlineListRemoteService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        }
    }
}
