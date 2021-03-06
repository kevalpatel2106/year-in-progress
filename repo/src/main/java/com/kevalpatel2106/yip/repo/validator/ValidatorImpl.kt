package com.kevalpatel2106.yip.repo.validator

import android.content.Context
import androidx.annotation.ColorInt
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.repo.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject

internal class ValidatorImpl @Inject constructor(
    @ApplicationContext private val application: Context
) : Validator {
    private val titleLength by lazy { application.resources.getInteger(R.integer.max_process_title) }
    private val descriptionLength by lazy { application.resources.getInteger(R.integer.max_process_description) }

    override fun isValidStartDate(startDate: Date?, endDate: Date?): Boolean {
        return endDate != null && startDate?.before(endDate) == true
    }

    override fun isValidEndDate(startDate: Date?, endDate: Date?): Boolean {
        return startDate != null && endDate?.after(startDate) == true
    }

    override fun isValidDeadlineColor(@ColorInt value: Int?): Boolean {
        return DeadlineColor.values().firstOrNull { it.colorInt == value } != null
    }

    override fun isValidTitle(title: String): Boolean {
        return title.trim().length in 1..titleLength
    }

    override fun isValidDescription(description: String?): Boolean {
        return if (description == null) true else description.trim().length in 0..descriptionLength
    }

    override fun isValidNotification(notification: List<Float>): Boolean {
        return !notification.any { it !in 0F..100F }
    }
}
