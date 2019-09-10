package com.kevalpatel2106.yip.repo.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import com.kevalpatel2106.yip.repo.db.ProgressTableInfo
import com.kevalpatel2106.yip.repo.utils.calculatePercent
import com.kevalpatel2106.yip.repo.utils.endOfTheDay
import com.kevalpatel2106.yip.repo.utils.endOfTheMonth
import com.kevalpatel2106.yip.repo.utils.endOfTheWeek
import com.kevalpatel2106.yip.repo.utils.endOfTheYear
import com.kevalpatel2106.yip.repo.utils.getFirstDayOfQuarter
import com.kevalpatel2106.yip.repo.utils.getLastDayOfQuarter
import com.kevalpatel2106.yip.repo.utils.startOfTheDay
import com.kevalpatel2106.yip.repo.utils.startOfTheMonth
import com.kevalpatel2106.yip.repo.utils.startOfTheWeek
import com.kevalpatel2106.yip.repo.utils.startOfTheYear
import com.kevalpatel2106.yip.repo.utils.toCal
import java.util.Date

@Entity(tableName = ProgressTableInfo.TABLE_NAME)
internal data class ProgressDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ProgressTableInfo.ID)
    var id: Long = 0,

    @ColumnInfo(name = ProgressTableInfo.TYPE)
    val progressType: ProgressType,

    @ColumnInfo(name = ProgressTableInfo.TITLE)
    val title: String,

    @ColumnInfo(name = ProgressTableInfo.START_TIME)
    var start: Date,

    @ColumnInfo(name = ProgressTableInfo.END_TIME)
    var end: Date,

    @ColumnInfo(name = ProgressTableInfo.COLOR)
    var color: ProgressColor = ProgressColor.COLOR_BLUE,

    @ColumnInfo(name = ProgressTableInfo.NOTIFICATIONS_PERCENTS)
    val notifications: List<Float> = listOf()
)

internal fun ProgressDto.toEntity(now: Date) = Progress(
    id = id,
    progressType = progressType,
    title = title,
    start = start,
    end = end,
    color = color,
    notificationPercent = notifications,
    percent = calculatePercent(now, start, end)
)

internal fun ProgressDto.modifyPrebuiltProgress(now: Date): ProgressDto {
    val nowCal = now.toCal()
    when (progressType) {
        ProgressType.YEAR_PROGRESS -> {
            start = nowCal.startOfTheYear()
            end = nowCal.endOfTheYear()
        }
        ProgressType.MONTH_PROGRESS -> {
            start = nowCal.startOfTheMonth()
            end = nowCal.endOfTheMonth()
        }
        ProgressType.WEEK_PROGRESS -> {
            start = nowCal.startOfTheWeek()
            end = nowCal.endOfTheWeek()
        }
        ProgressType.DAY_PROGRESS -> {
            start = nowCal.startOfTheDay()
            end = nowCal.endOfTheDay()
        }
        ProgressType.QUARTER_PROGRESS -> {
            start = nowCal.getFirstDayOfQuarter()
            end = nowCal.getLastDayOfQuarter()
        }
        ProgressType.CUSTOM -> {
            /* Do nothing. Go with the DB dates. */
        }
    }
    return this
}
