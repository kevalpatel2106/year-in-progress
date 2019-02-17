package com.kevalpatel2106.yip.repo.db

import androidx.annotation.ColorInt
import androidx.room.TypeConverter
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
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
    fun toLongList(value: String?): List<Float> {
        return value?.split(",")?.filter { it.isNotBlank() }?.map { it.toFloat() } ?: listOf()
    }

    @JvmStatic
    @TypeConverter
    fun toCommaSeparatedList(value: List<Float>): String {
        return value.joinToString(",")
    }

    @JvmStatic
    @TypeConverter
    fun toType(value: Int): ProgressType {
        return when (value) {
            ProgressType.YEAR_PROGRESS.key -> ProgressType.YEAR_PROGRESS
            ProgressType.MONTH_PROGRESS.key -> ProgressType.MONTH_PROGRESS
            ProgressType.WEEK_PROGRESS.key -> ProgressType.WEEK_PROGRESS
            ProgressType.DAY_PROGRESS.key -> ProgressType.DAY_PROGRESS
            ProgressType.QUARTER_PROGRESS.key -> ProgressType.QUARTER_PROGRESS
            else -> ProgressType.CUSTOM
        }
    }

    @JvmStatic
    @TypeConverter
    fun fromType(value: ProgressType): Int {
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
