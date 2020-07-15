package com.kevalpatel2106.yip.edit

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kevalpatel2106.yip.R
import kotlinx.android.synthetic.main.dialog_notification_time_picker.view.dialog_notification_percent_text
import kotlinx.android.synthetic.main.dialog_notification_time_picker.view.dialog_notification_seekbar
import java.util.Date

internal object EditDeadlineUseCases {

    internal fun EditDeadlineActivity.conformBeforeExit() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.edit_deadline_discard_confirm_message)
            .setPositiveButton(R.string.edit_deadline_discard_btn_title) { _, _ -> finish() }
            .setNegativeButton(R.string.edit_deadline_dismiss_btn_title) { dialog, _ -> dialog.cancel() }
            .show()
    }

    internal inline fun showDatePicker(
        fragmentManager: FragmentManager,
        startDateSelection: Date,
        endDateSelection: Date,
        crossinline listener: (startDate: Date, endDate: Date) -> Unit
    ) {
        MaterialDatePicker.Builder
            .dateRangePicker()
            .setTheme(R.style.YipComponentTheme_DatePickerDialog)
            .setSelection(Pair(startDateSelection.time, endDateSelection.time))
            .build()
            .apply {
                clearOnDismissListeners()
                addOnPositiveButtonClickListener { listener(Date(it.first!!), Date(it.second!!)) }
            }
            .show(fragmentManager, MaterialDatePicker::class.simpleName)
    }

    @SuppressLint("InflateParams")
    internal fun EditDeadlineActivity.showNotificationPickerDialog(onAdded: (percent: Float) -> Unit) {
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
}
