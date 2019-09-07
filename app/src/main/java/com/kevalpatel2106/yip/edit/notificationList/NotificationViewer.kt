package com.kevalpatel2106.yip.edit.notificationList

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.getColorCompat
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.payment.PaymentActivity
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import com.kevalpatel2106.yip.repo.utils.DateFormatter
import kotlinx.android.synthetic.main.dialog_notification_time_picker.view.dialog_notification_percent_text
import kotlinx.android.synthetic.main.dialog_notification_time_picker.view.dialog_notification_seekbar
import kotlinx.android.synthetic.main.row_notification_time.view.notification_delete_icon_iv
import kotlinx.android.synthetic.main.row_notification_time.view.notification_time_tv
import javax.inject.Inject

internal class NotificationViewer @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    diffStyleAttributes: Int = 0
) : LinearLayout(context, attributes, diffStyleAttributes) {

    internal var notificationPercents = mutableListOf<Float>()
        set(value) {
            removeAllViews()
            field = value
            value.forEach { addNotificationsRow(it) }
            addView(addNotificationView)
        }

    @Inject
    internal lateinit var dateFormatter: DateFormatter

    @Inject
    internal lateinit var billingRepo: BillingRepo

    private val inflater: LayoutInflater by lazy { LayoutInflater.from(context) }
    private val addNotificationView by lazy {
        inflater.inflate(R.layout.row_add_notification, this@NotificationViewer, false)
            .apply {
                setOnClickListener {
                    when {
                        notificationPercents.size == 0 -> showNotificationPickerDialog()  // First entry is free.
                        billingRepo.isPurchased() -> showNotificationPickerDialog()
                        else -> PaymentActivity.launch(context)
                    }
                }
            }
    }

    init {
        context.getAppComponent().inject(this@NotificationViewer)
        orientation = VERTICAL
    }

    private fun addNotificationsRow(notificationPercent: Float) {
        val view = inflater.inflate(R.layout.row_notification_time, this@NotificationViewer, false)

        val percentString = context.getString(R.string.progress_percentage, notificationPercent)
        val rawText = context.getString(R.string.row_notification_time_text, percentString)

        view.notification_time_tv.text = SpannableString(rawText).apply {
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
        view.notification_delete_icon_iv.setOnClickListener {
            notificationPercents.remove(notificationPercent)
            removeView(view)
        }
        addView(view)
    }

    private fun showNotificationPickerDialog() {
        AlertDialog.Builder(context).apply {
            var currentProgress = 0

            @SuppressLint("InflateParams")
            val dialogView = inflater.inflate(
                R.layout.dialog_notification_time_picker,
                this@NotificationViewer,
                false
            ).apply {
                dialog_notification_percent_text.text = context.getString(
                    R.string.set_notification_dialog_summary,
                    currentProgress
                )
                dialog_notification_seekbar.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        currentProgress = progress
                        dialog_notification_percent_text.text = context.getString(
                            R.string.set_notification_dialog_summary,
                            currentProgress
                        )
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
                    override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
                })
            }
            setView(dialogView)

            setCancelable(false)
            setTitle(R.string.set_notification_dialog_title)
            setNegativeButton(android.R.string.cancel, null)
            setPositiveButton(android.R.string.ok) { _, _ ->
                val newList = arrayListOf<Float>()
                newList.addAll(notificationPercents)
                newList.add(currentProgress.toFloat())
                notificationPercents = newList
            }
        }.show()
    }

    companion object {
        private const val RELATIVE_SPAN_FACTOR = 1.1F
    }
}
