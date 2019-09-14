package com.kevalpatel2106.yip.repo.dto

import androidx.annotation.VisibleForTesting
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressType
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

@VisibleForTesting
internal fun calculatePercent(now: Date, start: Date, end: Date): Float {
    val percent = (now.time - start.time) * 100 / (end.time - start.time).toFloat()
    return when {
        percent > 100f -> 100f
        percent < 0f -> 0f
        else -> percent
    }
}
