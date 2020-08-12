package com.kevalpatel2106.yip.detail

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.TypefaceSpan
import android.view.Menu
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.pm.ShortcutManagerCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.ext.emptySpannableString
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.ext.isFinished
import com.kevalpatel2106.yip.entity.ext.timeLeftDHM

internal object DetailUseCase {

    internal fun preparePopupMenu(
        anchor: View,
        supportsPinning: Boolean = ShortcutManagerCompat.isRequestPinShortcutSupported(anchor.context),
        clickListener: PopupMenu.OnMenuItemClickListener
    ): PopupMenu {
        return PopupMenu(anchor.context, anchor).apply {
            menu.add(
                R.id.menu_deadline_group,
                R.id.menu_delete_deadline,
                Menu.NONE,
                anchor.context.getString(R.string.meu_title_delete)
            )
            if (supportsPinning) {
                menu.add(
                    R.id.menu_deadline_group,
                    R.id.menu_pin_shortcut,
                    Menu.NONE,
                    anchor.context.getString(R.string.meu_title_pin_shortcut)
                )
            }
            setOnMenuItemClickListener(clickListener)
        }
    }

    @Suppress("MagicNumber")
    internal fun prepareTimeLeft(
        application: Context,
        deadline: Deadline,
        @ColorInt secondaryColor: Int
    ): SpannableString {
        if (deadline.isFinished()) return emptySpannableString()
        val (days, hours, mins) = deadline.timeLeftDHM()

        // Prepare raw string
        val rawString = application.getString(
            R.string.time_left_title,
            days,
            application.resources.getQuantityString(R.plurals.days, days.toInt()),
            hours,
            application.resources.getQuantityString(R.plurals.hours, hours.toInt()),
            mins,
            application.resources.getQuantityString(R.plurals.minutes, mins.toInt())
        )

        return SpannableString(rawString).apply {
            setUpSpans(0, rawString.indexOfFirst { it == '\n' }, secondaryColor)

            // Set spans for days left
            val dayStartIndex = rawString.indexOf(days.toString())
            val dayEndIndex = rawString.indexOf(days.toString()) + days.toString().length
            setUpSpans(dayStartIndex, dayEndIndex, secondaryColor)

            // Set spans for hours left
            val hoursStartIndex = rawString.indexOf(hours.toString(), dayEndIndex)
            val hoursEndIndex = hoursStartIndex + hours.toString().length
            setUpSpans(hoursStartIndex, hoursEndIndex, secondaryColor)

            // Set spans for minutes left
            val minsStartIndex = rawString.indexOf(mins.toString(), hoursEndIndex)
            val minsEndIndex = minsStartIndex + mins.toString().length
            setUpSpans(minsStartIndex, minsEndIndex, secondaryColor)
        }
    }

    private fun SpannableString.setUpSpans(startIndex: Int, endIndex: Int, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            setSpan(
                TypefaceSpan(Typeface.DEFAULT_BOLD),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        setSpan(
            ForegroundColorSpan(color),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setSpan(
            RelativeSizeSpan(TIME_LEFT_RELATIVE_SIZE_FACTOR),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    internal fun conformDelete(context: Context, title: String, onDelete: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setMessage(context.getString(R.string.deadline_delete_confirmation_message, title))
            .setPositiveButton(context.getString(R.string.deadline_delete_confirmation_delete_title)) { _, _ -> onDelete() }
            .setNegativeButton(android.R.string.cancel, null)
            .setCancelable(true)
            .show()
    }

    private const val TIME_LEFT_RELATIVE_SIZE_FACTOR = 1.1F
}
