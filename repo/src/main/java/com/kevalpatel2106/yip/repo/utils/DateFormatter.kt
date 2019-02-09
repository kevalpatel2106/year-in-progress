package com.kevalpatel2106.yip.repo.utils

import android.annotation.SuppressLint
import android.app.Application
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.providers.SharedPrefsProvider
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@SuppressLint("CheckResult")
class DateFormatter @Inject constructor(
        application: Application,
        sharedPrefsProvider: SharedPrefsProvider
) {
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

    fun format(date: Date): String {
        return SimpleDateFormat("$dateFormat $timeFormat", Locale.getDefault()).format(date)
    }

    fun formatDateOnly(date: Date): String {
        return SimpleDateFormat(dateFormat, Locale.getDefault()).format(date)
    }
}