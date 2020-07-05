package com.kevalpatel2106.yip.repo.dateFormatter

import android.annotation.SuppressLint
import android.content.Context
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("CheckResult")
internal class DateFormatterImpl internal constructor(
    application: Context,
    sharedPrefsProvider: SharedPrefsProvider
) : DateFormatter {
    private var dateFormat: String = application.getString(R.string.dd_MMM_yyyy)
    private var timeFormat: String = application.getString(R.string.hh_mm_a)

    init {
        sharedPrefsProvider.observeStringFromPreference(
            application.getString(R.string.pref_key_date_format),
            application.getString(R.string.dd_MMM_yyyy)
        ).subscribe {
            dateFormat = it
        }

        sharedPrefsProvider.observeStringFromPreference(
            application.getString(R.string.pref_key_time_format),
            application.getString(R.string.hh_mm_a)
        ).subscribe {
            timeFormat = it
        }
    }

    override fun format(date: Date): String {
        return SimpleDateFormat("$dateFormat $timeFormat", Locale.getDefault()).format(date)
    }

    override fun formatDateOnly(date: Date): String {
        return SimpleDateFormat(dateFormat, Locale.getDefault()).format(date)
    }
}
