package com.kevalpatel2106.yip.notifications

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.Progress

/**
 * Helper class for showing and canceling progress
 * notifications.
 *
 *
 * This class makes heavy use of the [NotificationCompat.Builder] helper
 * class to create notifications in a backward-compatible way.
 */
object ProgressNotification {
    private const val CHANNEL_ID = "deadline_notification"
    /**
     * The unique identifier for this type of notification.
     */
    private const val NOTIFICATION_TAG = 3784

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * @see .cancel
     */
    fun notify(context: Context, progress: Progress) {
        val title = getTitle(progress)
        val message = getMessage(progress)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setTicker(message)
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")),
                                PendingIntent.FLAG_UPDATE_CURRENT)
                )
                .setStyle(
                        NotificationCompat.BigTextStyle()
                                .bigText(message)
                                .setBigContentTitle(title)
                                .setSummaryText(message)
                )
                .setAutoCancel(true)

        notify(context, builder.build())
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private fun notify(context: Context, notification: Notification) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(NotificationChannel(
                    CHANNEL_ID,
                    "Deadline notifications",
                    NotificationManager.IMPORTANCE_HIGH
            ))
        }
        nm.notify(NOTIFICATION_TAG, notification)
    }

    private fun getTitle(progress: Progress): String {
        return "This is the title"
    }

    private fun getMessage(progress: Progress): String {
        return "This is the message"
    }
}
