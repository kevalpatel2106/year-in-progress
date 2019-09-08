package com.kevalpatel2106.yip.repo.utils

import android.app.Application
import androidx.annotation.ColorInt
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.repo.R
import java.util.Date
import javax.inject.Inject

class Validator @Inject internal constructor(private val application: Application) {
    private val titleLength by lazy { application.resources.getInteger(R.integer.max_process_title) }

    fun isValidStartDate(startDate: Date?, endDate: Date?): Boolean {
        return endDate != null && startDate?.before(endDate) == true
    }

    fun isValidEndDate(startDate: Date?, endDate: Date?): Boolean {
        return startDate != null && endDate?.after(startDate) == true
    }

    fun isValidProgressColor(@ColorInt value: Int?): Boolean {
        return ProgressColor.values().firstOrNull { it.colorInt == value } != null
    }

    fun isValidTitle(title: String): Boolean {
        return title.length in 1..titleLength
    }

    fun isValidNotification(notification: List<Float>): Boolean {
        return !notification.any { it !in 0F..100F }
    }
}
