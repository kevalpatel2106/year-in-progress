package com.kevalpatel2106.yip.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.VisibleForTesting
import androidx.core.app.NotificationCompat
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.utils.AppLaunchHelper

/**
 * Helper class for showing and canceling progress
 * notifications.
 *
 *
 * This class makes heavy use of the [NotificationCompat.Builder] helper
 * class to create notifications in a backward-compatible way.
 */
object ProgressNotification {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal const val CHANNEL_ID = "deadline_notification"

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal const val COMPLETE_DOT = "|"

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal const val INCOMPLETE_DOT = ":"

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * @see .cancel
     */
    @SuppressLint("NewApi")
    fun notify(context: Context, progress: Progress) {
        val notificationId = generateNotificationId(progress.id)
        val title = getTitle(context, progress.title, progress.percent)
        val message = getMessage(context, progress.percent)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setTicker(message)
            .setContentIntent(getPendingIntent(context, notificationId, progress.id))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
                    .setBigContentTitle(title)
            )
            .setAutoCancel(true)

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        getDeadlineNotificationChannel(context)?.let { nm.createNotificationChannel(it) }
        nm.notify(notificationId, builder.build())
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getTitle(context: Context, progressTitle: String, percent: Float): String {
        return String.format(
            context.getString(R.string.progress_notification_title),
            progressTitle,
            percent
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getMessage(context: Context, percent: Float): String {
        var dots = ""
        repeat(percent.toInt() / 2) { dots += COMPLETE_DOT }
        repeat((100 - percent.toInt()) / 2) { dots += INCOMPLETE_DOT }
        return "$dots ${String.format(context.getString(R.string.progress_percentage), percent)}"
    }

    private fun getPendingIntent(
        context: Context,
        notificationId: Int,
        progressId: Long
    ): PendingIntent {
        return PendingIntent.getActivity(
            context,
            notificationId,
            AppLaunchHelper.launchWithProgressDetail(context, progressId),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateNotificationId(progressId: Long): Int = progressId.toInt()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getDeadlineNotificationChannel(context: Context): NotificationChannel? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.deadline_notification_channel_title),
                NotificationManager.IMPORTANCE_HIGH
            )
        } else {
            null
        }
    }
}
