package com.kevalpatel2106.yip.repo.utils.validator

import androidx.annotation.ColorInt
import java.util.Date

interface Validator {
    fun isValidStartDate(startDate: Date?, endDate: Date?): Boolean
    fun isValidEndDate(startDate: Date?, endDate: Date?): Boolean
    fun isValidDeadlineColor(@ColorInt value: Int?): Boolean
    fun isValidTitle(title: String): Boolean
    fun isValidNotification(notification: List<Float>): Boolean
}

