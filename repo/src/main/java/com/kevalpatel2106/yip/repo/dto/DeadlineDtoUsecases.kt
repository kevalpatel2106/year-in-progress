package com.kevalpatel2106.yip.repo.dto

import androidx.annotation.VisibleForTesting
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineType
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


internal fun DeadlineDto.toEntity(now: Date) = Deadline(
    id = id,
    deadlineType = type,
    title = title,
    start = start,
    end = end,
    color = color,
    notificationPercent = notifications,
    percent = calculatePercent(now, start, end)
)

internal fun DeadlineDto.modifyPrebuiltDeadline(now: Date): DeadlineDto {
    val nowCal = now.toCal()
    when (type) {
        DeadlineType.YEAR_DEADLINE -> {
            start = nowCal.startOfTheYear()
            end = nowCal.endOfTheYear()
        }
        DeadlineType.MONTH_DEADLINE -> {
            start = nowCal.startOfTheMonth()
            end = nowCal.endOfTheMonth()
        }
        DeadlineType.WEEK_DEADLINE -> {
            start = nowCal.startOfTheWeek()
            end = nowCal.endOfTheWeek()
        }
        DeadlineType.DAY_DEADLINE -> {
            start = nowCal.startOfTheDay()
            end = nowCal.endOfTheDay()
        }
        DeadlineType.QUARTER_DEADLINE -> {
            start = nowCal.getFirstDayOfQuarter()
            end = nowCal.getLastDayOfQuarter()
        }
        DeadlineType.CUSTOM -> {
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
