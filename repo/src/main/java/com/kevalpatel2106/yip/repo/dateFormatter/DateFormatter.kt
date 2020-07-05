package com.kevalpatel2106.yip.repo.dateFormatter

import java.util.Date

interface DateFormatter {
    fun format(date: Date): String
    fun formatDateOnly(date: Date): String
}
