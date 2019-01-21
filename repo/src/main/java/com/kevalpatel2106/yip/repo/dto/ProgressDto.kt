package com.kevalpatel2106.yip.repo.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import com.kevalpatel2106.yip.repo.utils.*
import com.kevalpatel2106.yip.repo.utils.db.ProgressTableInfo
import java.util.*

@Entity(tableName = ProgressTableInfo.TABLE_NAME)
internal data class ProgressDto(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ProgressTableInfo.ID)
        var id: Long = 0,

        @ColumnInfo(name = ProgressTableInfo.ORDER)
        val order: Int,

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

        @ColumnInfo(name = ProgressTableInfo.IS_ENABLED)
        var isEnabled: Boolean = true
)

internal fun ProgressDto.toEntity() = Progress(
        id = id,
        order = order,
        progressType = progressType,
        title = title,
        start = start,
        end = end,
        color = color,
        isEnabled = isEnabled
)

internal fun ProgressDto.modifyPrebuiltProgress(ntpProvider: NtpProvider): ProgressDto {
    val now = Calendar.getInstance()
    now.timeInMillis = ntpProvider.now().time

    when (progressType) {
        ProgressType.YEAR_PROGRESSType -> {
            start = now.startOfTheYear()
            end = now.endOfTheYear()
        }
        ProgressType.MONTH_PROGRESSType -> {
            start = now.startOfTheMonth()
            end = now.endOfTheMonth()
        }
        ProgressType.WEEK_PROGRESSType -> {
            start = now.startOfTheWeek()
            end = now.endOfTheWeek()
        }
        ProgressType.DAY_PROGRESSType -> {
            start = now.startOfTheDay()
            end = now.endOfTheDay()
        }
        ProgressType.QUARTER_PROGRESSType -> {
            start = now.getFirstDayOfQuarter()
            end = now.getLastDayOfQuarter()
        }
        ProgressType.CUSTOM -> {
            /* Do nothing. Go with the DB dates. */
        }
    }
    return this
}
