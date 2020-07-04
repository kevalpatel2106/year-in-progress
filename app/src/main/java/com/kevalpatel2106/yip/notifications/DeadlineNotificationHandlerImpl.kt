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
import com.kevalpatel2106.yip.core.emptyString
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.utils.AppLaunchHelper

internal class DeadlineNotificationHandlerImpl(
    private val context: Context,
    private val appLaunchHelper: AppLaunchHelper
) : DeadlineNotificationHandler {

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * @see [NotificationManager.cancel]
     */
    @SuppressLint("NewApi")
    override fun notify(deadline: Deadline) {
        val notificationId = generateNotificationId(deadline.id)
        val title = getTitle(context, deadline.title, deadline.percent)
        val message = getMessage(context, deadline.percent)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setTicker(message)
            .setContentIntent(getPendingIntent(context, notificationId, deadline.id))
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
    internal fun getTitle(context: Context, title: String, percent: Float): String {
        return String.format(
            context.getString(R.string.deadline_notification_title),
            title,
            percent
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getMessage(context: Context, percent: Float): String {
        var dots = emptyString()
        repeat(percent.toInt() / 2) { dots += COMPLETE_DOT }
        repeat((100 - percent.toInt()) / 2) { dots += INCOMPLETE_DOT }
        return "$dots ${String.format(context.getString(R.string.deadline_percentage), percent)}"
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getPendingIntent(context: Context, notificationId: Int, id: Long): PendingIntent {
        return PendingIntent.getActivity(
            context,
            notificationId,
            appLaunchHelper.getLaunchIntentWithDeadlineDetail(context, id),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateNotificationId(id: Long): Int = id.toInt()

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

    companion object {
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        internal const val CHANNEL_ID = "deadline_notification"

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        internal const val COMPLETE_DOT = "|"

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        internal const val INCOMPLETE_DOT = ":"
    }
}
