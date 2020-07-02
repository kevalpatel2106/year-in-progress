package com.kevalpatel2106.yip.repo.db

import androidx.annotation.ColorInt
import androidx.room.TypeConverter
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
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
    fun toType(value: Int): DeadlineType {
        return when (value) {
            DeadlineType.YEAR_DEADLINE.key -> DeadlineType.YEAR_DEADLINE
            DeadlineType.MONTH_DEADLINE.key -> DeadlineType.MONTH_DEADLINE
            DeadlineType.WEEK_DEADLINE.key -> DeadlineType.WEEK_DEADLINE
            DeadlineType.DAY_DEADLINE.key -> DeadlineType.DAY_DEADLINE
            DeadlineType.QUARTER_DEADLINE.key -> DeadlineType.QUARTER_DEADLINE
            else -> DeadlineType.CUSTOM
        }
    }

    @JvmStatic
    @TypeConverter
    fun fromType(value: DeadlineType): Int = value.key

    @JvmStatic
    @TypeConverter
    fun toDeadlineColor(@ColorInt value: Int): DeadlineColor {
        return when (value) {
            DeadlineColor.COLOR_BLUE.colorInt -> DeadlineColor.COLOR_BLUE
            DeadlineColor.COLOR_GREEN.colorInt -> DeadlineColor.COLOR_GREEN
            DeadlineColor.COLOR_TILL.colorInt -> DeadlineColor.COLOR_TILL
            DeadlineColor.COLOR_ORANGE.colorInt -> DeadlineColor.COLOR_ORANGE
            DeadlineColor.COLOR_YELLOW.colorInt -> DeadlineColor.COLOR_YELLOW
            DeadlineColor.COLOR_PINK.colorInt -> DeadlineColor.COLOR_PINK
            DeadlineColor.COLOR_GRAY.colorInt -> DeadlineColor.COLOR_GRAY
            else -> DeadlineColor.COLOR_GRAY
        }
    }

    @ColorInt
    @JvmStatic
    @TypeConverter
    fun fromColor(value: DeadlineColor): Int = value.colorInt
}
