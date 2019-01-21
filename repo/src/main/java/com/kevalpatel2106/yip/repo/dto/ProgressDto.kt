package com.kevalpatel2106.yip.repo.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kevalpatel2106.yip.entity.PrebuiltProgress
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.repo.utils.*
import java.util.*

@Entity(tableName = "progresses")
internal data class ProgressDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "order")
    val order: Int,

    @ColumnInfo(name = "type")
    val prebuiltProgress: PrebuiltProgress,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "start_mills")
    var start: Date,

    @ColumnInfo(name = "end_mills")
    var end: Date,

    @ColumnInfo(name = "color")
    var color: ProgressColor = ProgressColor.COLOR_BLUE,

    @ColumnInfo(name = "is_enabled")
    var isEnabled: Boolean = true
)

internal fun ProgressDto.toEntity() = Progress(
    id = id,
    order = order,
    prebuiltProgress = prebuiltProgress,
    title = title,
    start = start,
    end = end,
    color = color,
    isEnabled = isEnabled
)

internal fun ProgressDto.modifyPrebuiltProgress(ntpProvider: NtpProvider): ProgressDto {
    val now = Calendar.getInstance()
    now.timeInMillis = ntpProvider.now().time

    when (prebuiltProgress) {
        PrebuiltProgress.YEAR_PROGRESS -> {
            start = now.startOfTheYear()
            end = now.endOfTheYear()
        }
        PrebuiltProgress.MONTH_PROGRESS -> {
            start = now.startOfTheMonth()
            end = now.endOfTheMonth()
        }
        PrebuiltProgress.WEEK_PROGRESS -> {
            start = now.startOfTheWeek()
            end = now.endOfTheWeek()
        }
        PrebuiltProgress.DAY_PROGRESS -> {
            start = now.startOfTheDay()
            end = now.endOfTheDay()
        }
        PrebuiltProgress.QUARTER_PROGRESS -> {
            start = now.getFirstDayOfQuarter()
            end = now.getLastDayOfQuarter()
        }
        PrebuiltProgress.CUSTOM -> {
            /* Do nothing. Go with the DB dates. */
        }
    }
    return this
}
