package com.kevalpatel2106.yip.edit

import android.app.DatePickerDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kevalpatel2106.yip.R
import java.util.Calendar
import java.util.Date

internal object EditProgressUseCases {

    internal fun EditProgressActivity.conformBeforeExit() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.edit_progress_discard_confirm_message)
            .setPositiveButton(R.string.edit_progress_discard_btn_title) { _, _ -> finish() }
            .setNegativeButton(R.string.edit_progress_dismiss_btn_title) { dialog, _ -> dialog.cancel() }
            .show()
    }

    internal fun EditProgressActivity.getDatePicker(
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
