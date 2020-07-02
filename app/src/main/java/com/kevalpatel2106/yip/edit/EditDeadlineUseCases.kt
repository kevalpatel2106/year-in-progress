package com.kevalpatel2106.yip.edit

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kevalpatel2106.yip.R
import kotlinx.android.synthetic.main.dialog_notification_time_picker.view.dialog_notification_percent_text
import kotlinx.android.synthetic.main.dialog_notification_time_picker.view.dialog_notification_seekbar
import java.util.Calendar
import java.util.Date

internal object EditDeadlineUseCases {

    internal fun EditDeadlineActivity.conformBeforeExit() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.edit_deadline_discard_confirm_message)
            .setPositiveButton(R.string.edit_deadline_discard_btn_title) { _, _ -> finish() }
            .setNegativeButton(R.string.edit_deadline_dismiss_btn_title) { dialog, _ -> dialog.cancel() }
            .show()
    }

    internal fun EditDeadlineActivity.getDatePicker(
        preset: Calendar = Calendar.getInstance(),
        listener: (date: Date) -> Unit
    ): DatePickerDialog {
        return DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val cal = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 0, 0)
                }
                listener.invoke(Date(cal.timeInMillis))
            },
            preset.get(Calendar.YEAR),
            preset.get(Calendar.MONTH),
            preset.get(Calendar.DAY_OF_MONTH)
        )
    }
}

@SuppressLint("InflateParams")
internal fun Context.showNotificationPickerDialog(onAdded: (percent: Float) -> Unit) {
    AlertDialog.Builder(this).apply {
        var currentPercent = 0
        val dialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.dialog_notification_time_picker, null)
            .apply {

                dialog_notification_percent_text.text = context.getString(
                    R.string.set_notification_dialog_summary,
                    currentPercent
                )
                dialog_notification_seekbar.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        currentPercent = progress
                        dialog_notification_percent_text.text = context.getString(
                            R.string.set_notification_dialog_summary,
                            currentPercent
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
        setPositiveButton(android.R.string.ok) { _, _ -> onAdded(currentPercent.toFloat()) }
    }.show()
}
