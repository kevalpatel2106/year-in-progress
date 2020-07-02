package com.kevalpatel2106.yip.edit.notificationList

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.getColorCompat
import kotlinx.android.synthetic.main.row_notification_time.view.notification_delete_icon_iv
import kotlinx.android.synthetic.main.row_notification_time.view.notification_time_tv


internal class NotificationViewer @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    diffStyleAttributes: Int = 0
) : LinearLayout(context, attributes, diffStyleAttributes) {

    internal var isLocked: Boolean = false
    internal var callback: NotificationViewerInterface? = null
    private val inflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    init {
        orientation = VERTICAL
    }

    internal fun updateList(notifications: List<Float>) {
        removeAllViews()
        notifications.forEach { addNotificationsRow(it) }
        addNewNotificationRow()
    }

    private fun addNotificationsRow(notificationPercent: Float) {
        val view = inflater.inflate(R.layout.row_notification_time, this@NotificationViewer, false)
        view.notification_time_tv.text = prepareString(notificationPercent)
        view.notification_delete_icon_iv.setOnClickListener {
            removeView(view)
            callback?.onNotificationRemoved(notificationPercent)
        }
        addView(view)
    }

    private fun prepareString(notificationPercent: Float): SpannableString {
        val percentString = context.getString(R.string.deadline_percentage, notificationPercent)
        val rawText = context.getString(R.string.row_notification_time_text, percentString)

        return SpannableString(rawText).apply {
            // Set spans for hours left
            val dateStartIndex = rawText.indexOf(percentString)
            val dateEndIndex = dateStartIndex + percentString.length
            setSpan(
                ForegroundColorSpan(context.getColorCompat(R.color.colorPrimaryText)),
                dateStartIndex,
                dateEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                RelativeSizeSpan(RELATIVE_SPAN_FACTOR),
                dateStartIndex,
                dateEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun addNewNotificationRow() {
        val view = inflater.inflate(R.layout.row_add_notification, this@NotificationViewer, false)
        view.setOnClickListener {
            if (isLocked) {
                callback?.onLockClicked()
            } else {
                callback?.addNotificationClicked()
            }
        }
        addView(view)
    }

    interface NotificationViewerInterface {
        fun onLockClicked()
        fun addNotificationClicked()
        fun onNotificationRemoved(percent: Float)
    }

    companion object {
        private const val RELATIVE_SPAN_FACTOR = 1.1F
    }
}
