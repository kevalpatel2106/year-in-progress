package com.kevalpatel2106.yip.repo.db

import androidx.annotation.ColorInt
import androidx.room.TypeConverter
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import java.util.Date

internal object DbTypeConverter {

    @JvmStatic
    @TypeConverter
    fun toDate(value: Long): Date = Date(value)

    @JvmStatic
    @TypeConverter
    fun toLong(value: Date): Long = value.time

    @JvmStatic
    @TypeConverter
    fun toFloatsList(value: String?): List<Float> {
        return value?.split(",")?.filter { it.isNotBlank() }?.map { it.toFloat() } ?: listOf()
    }

    @JvmStatic
    @TypeConverter
    fun toCommaSeparatedList(value: List<Float>): String = value.joinToString(",")

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
    fun fromType(value: ProgressType): Int = value.key

    @JvmStatic
    @TypeConverter
    fun toProgressColor(@ColorInt value: Int): ProgressColor {
        return when (value) {
            ProgressColor.COLOR_BLUE.colorInt -> ProgressColor.COLOR_BLUE
            ProgressColor.COLOR_GREEN.colorInt -> ProgressColor.COLOR_GREEN
            ProgressColor.COLOR_TILL.colorInt -> ProgressColor.COLOR_TILL
            ProgressColor.COLOR_ORANGE.colorInt -> ProgressColor.COLOR_ORANGE
            ProgressColor.COLOR_YELLOW.colorInt -> ProgressColor.COLOR_YELLOW
            ProgressColor.COLOR_PINK.colorInt -> ProgressColor.COLOR_PINK
            ProgressColor.COLOR_GRAY.colorInt -> ProgressColor.COLOR_GRAY
            else -> ProgressColor.COLOR_GRAY
        }
    }

    @ColorInt
    @JvmStatic
    @TypeConverter
    fun fromColor(value: ProgressColor): Int = value.colorInt
}
