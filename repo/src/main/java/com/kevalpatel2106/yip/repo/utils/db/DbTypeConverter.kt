package com.kevalpatel2106.yip.repo.utils.db

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
    fun toType(value: Int): ProgressType {
        return when (value) {
            ProgressType.YEAR_PROGRESSType.key -> ProgressType.YEAR_PROGRESSType
            ProgressType.MONTH_PROGRESSType.key -> ProgressType.MONTH_PROGRESSType
            ProgressType.WEEK_PROGRESSType.key -> ProgressType.WEEK_PROGRESSType
            ProgressType.DAY_PROGRESSType.key -> ProgressType.DAY_PROGRESSType
            ProgressType.QUARTER_PROGRESSType.key -> ProgressType.QUARTER_PROGRESSType
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
