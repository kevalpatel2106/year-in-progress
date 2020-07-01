package com.kevalpatel2106.yip.repo.utils.validator

import android.content.Context
import androidx.annotation.ColorInt
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.repo.R
import java.util.Date

internal class ValidatorImpl constructor(private val application: Context) :
    Validator {
    private val titleLength by lazy { application.resources.getInteger(R.integer.max_process_title) }

    override fun isValidStartDate(startDate: Date?, endDate: Date?): Boolean {
        return endDate != null && startDate?.before(endDate) == true
    }

    override fun isValidEndDate(startDate: Date?, endDate: Date?): Boolean {
        return startDate != null && endDate?.after(startDate) == true
    }

    override fun isValidProgressColor(@ColorInt value: Int?): Boolean {
        return ProgressColor.values().firstOrNull { it.colorInt == value } != null
    }

    override fun isValidTitle(title: String): Boolean {
        return title.length in 1..titleLength
    }

    override fun isValidNotification(notification: List<Float>): Boolean {
        return !notification.any { it !in 0F..100F }
    }
}
