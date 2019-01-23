package com.kevalpatel2106.yip.base

import android.app.Application
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.repo.utils.SharedPrefsProvider
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateFormatter @Inject constructor(
        application: Application,
        private val sharedPrefsProvider: SharedPrefsProvider
) {

    private val prefKeyDateFormat by lazy { application.getString(R.string.pref_key_date_format) }
    private val defaultDateFormat by lazy { application.resources.getStringArray(R.array.date_format_values)[0] }
    private val prefKeyTimeFormat by lazy { application.getString(R.string.pref_key_time_format) }
    private val defaultTimeFormat by lazy { application.resources.getStringArray(R.array.time_format_values)[0] }

    fun format(date: Date): String {
        return SimpleDateFormat(
                "${sharedPrefsProvider.getStringFromPreferences(prefKeyDateFormat, defaultDateFormat)} ${sharedPrefsProvider.getStringFromPreferences(prefKeyTimeFormat, defaultTimeFormat)}",
                Locale.getDefault()
        ).format(date)
    }

    fun formatDateOnly(date: Date): String {
        return SimpleDateFormat(
                "${sharedPrefsProvider.getStringFromPreferences(prefKeyDateFormat, defaultDateFormat)}",
                Locale.getDefault()
        ).format(date)
    }
}