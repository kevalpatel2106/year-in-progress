package com.kevalpatel2106.yip.detail

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
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
import com.kevalpatel2106.yip.R
import java.util.Date
import java.util.concurrent.TimeUnit

internal object DetailUseCase {

    internal fun preparePopupMenu(
        anchor: View,
        supportsPinningShortcut: Boolean = ShortcutManagerCompat
            .isRequestPinShortcutSupported(anchor.context)
    ): PopupMenu {
        return PopupMenu(anchor.context, anchor).apply {
            menu.add(
                R.id.menu_progress_group,
                R.id.menu_delete_progress,
                Menu.NONE,
                anchor.context.getString(R.string.meu_title_delete)
            )

            if (supportsPinningShortcut) {
                menu.add(
                    R.id.menu_progress_group,
                    R.id.menu_pin_shortcut,
                    Menu.NONE,
                    anchor.context.getString(R.string.meu_title_pin_shortcut)
                )
            }
        }
    }

    internal fun prepareShareAchievementIntent(context: Context, title: String?): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            title?.let { title ->
                putExtra(
                    Intent.EXTRA_TEXT,
                    context.getString(R.string.achivement_share_message, title)
                )
            }
            type = "text/plain"
        }
    }

    @Suppress("MagicNumber")
    internal fun prepareTimeLeft(
        application: Application,
        endTime: Date,
        @ColorInt secondaryColor: Int
    ): SpannableString {

        // Find difference in mills
        var diffMills = endTime.time - System.currentTimeMillis()
        if (diffMills < 0) return SpannableString("")

        // Calculate the days, hours and minutes
        val days = TimeUnit.DAYS.convert(diffMills, TimeUnit.MILLISECONDS)
        if (days != 0L) diffMills %= TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS)

        val hours = TimeUnit.HOURS.convert(diffMills, TimeUnit.MILLISECONDS)
        if (hours != 0L) diffMills %= TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS)

        val mins = TimeUnit.MINUTES.convert(diffMills, TimeUnit.MILLISECONDS)

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
        AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
            .setMessage(context.getString(R.string.progress_delete_confirmation_message, title))
            .setPositiveButton(context.getString(R.string.progress_delete_confirmation_delete_title)) { _, _ -> onDelete() }
            .setNegativeButton(android.R.string.cancel, null)
            .setCancelable(true)
            .show()
    }

    private const val TIME_LEFT_RELATIVE_SIZE_FACTOR = 1.3F
}
