package com.kevalpatel2106.yip.detail

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.pm.ShortcutManagerCompat
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.ProgressColor
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
    fun prepareTimeLeft(
        application: Application,
        endTime: Date,
        progressColor: ProgressColor
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
            setSpan(
                RelativeSizeSpan(0.8f),
                0,
                rawString.indexOfFirst { it == '\n' },
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Set spans for days left
            val dayStartIndex = rawString.indexOf(days.toString())
            val dayEndIndex = rawString.indexOf(days.toString()) + days.toString().length
            setSpan(
                ForegroundColorSpan(progressColor.value),
                dayStartIndex,
                dayEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                RelativeSizeSpan(1.3f),
                dayStartIndex,
                dayEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Set spans for hours left
            val hoursStartIndex = rawString.indexOf(hours.toString(), dayEndIndex)
            val hoursEndIndex = hoursStartIndex + hours.toString().length
            setSpan(
                ForegroundColorSpan(progressColor.value),
                hoursStartIndex,
                hoursEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                RelativeSizeSpan(1.3f),
                hoursStartIndex,
                hoursEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Set spans for minutes left
            val minsStartIndex = rawString.indexOf(mins.toString(), hoursEndIndex)
            val minsEndIndex = minsStartIndex + mins.toString().length
            setSpan(
                ForegroundColorSpan(progressColor.value),
                minsStartIndex,
                minsEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                RelativeSizeSpan(1.3f),
                minsStartIndex,
                minsEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    internal fun conformDelete(context: Context, title: String, onDelete: () -> Unit) {
        AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
            .setMessage(context.getString(R.string.progress_delete_confirmation_message, title))
            .setPositiveButton(context.getString(R.string.progress_delete_confirmation_delete_title)) { _, _ -> onDelete() }
            .setNegativeButton(android.R.string.cancel, null)
            .setCancelable(true)
            .show()
    }
}
