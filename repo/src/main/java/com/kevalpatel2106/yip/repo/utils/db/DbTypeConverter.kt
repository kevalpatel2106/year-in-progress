package com.kevalpatel2106.yip.repo.utils.db

import androidx.annotation.ColorInt
import androidx.room.TypeConverter
import com.kevalpatel2106.yip.entity.PrebuiltProgress
import com.kevalpatel2106.yip.entity.ProgressColor
import java.util.*

internal object DbTypeConverter {

    @JvmStatic
    @TypeConverter
    fun toDate(value: Long): Date? {
        return Date(value)
    }

    @JvmStatic
    @TypeConverter
    fun toLong(value: Date): Long {
        return value.time
    }

    @JvmStatic
    @TypeConverter
    fun toType(value: Int): PrebuiltProgress {
        return when (value) {
            PrebuiltProgress.YEAR_PROGRESS.key -> PrebuiltProgress.YEAR_PROGRESS
            PrebuiltProgress.MONTH_PROGRESS.key -> PrebuiltProgress.MONTH_PROGRESS
            PrebuiltProgress.WEEK_PROGRESS.key -> PrebuiltProgress.WEEK_PROGRESS
            PrebuiltProgress.DAY_PROGRESS.key -> PrebuiltProgress.DAY_PROGRESS
            PrebuiltProgress.QUARTER_PROGRESS.key -> PrebuiltProgress.QUARTER_PROGRESS
            else -> PrebuiltProgress.CUSTOM
        }
    }

    @JvmStatic
    @TypeConverter
    fun fromType(value: PrebuiltProgress): Int {
        return value.key
    }

    @JvmStatic
    @TypeConverter
    fun toProgressColor(@ColorInt value: Int): ProgressColor {
        return when (value) {
            ProgressColor.COLOR_BLUE.value -> ProgressColor.COLOR_BLUE
            ProgressColor.COLOR_GREEN.value -> ProgressColor.COLOR_GREEN
            ProgressColor.COLOR_GREY.value -> ProgressColor.COLOR_GREY
            ProgressColor.COLOR_ORANGE.value -> ProgressColor.COLOR_ORANGE
            ProgressColor.COLOR_YELLOW.value -> ProgressColor.COLOR_YELLOW
            else -> ProgressColor.COLOR_BLUE
        }
    }

    @ColorInt
    @JvmStatic
    @TypeConverter
    fun fromColor(value: ProgressColor): Int {
        return value.value
    }
}
