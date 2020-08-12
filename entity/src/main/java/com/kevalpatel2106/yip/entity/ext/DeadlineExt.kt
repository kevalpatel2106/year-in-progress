package com.kevalpatel2106.yip.entity.ext

import androidx.annotation.VisibleForTesting
import com.kevalpatel2106.yip.entity.Deadline
import java.util.concurrent.TimeUnit

fun Deadline.isFinished(@VisibleForTesting now: Long = System.currentTimeMillis()): Boolean {
    return end.time - now <= 0
}

fun Deadline.timeLeftDHM(): Triple<Long, Long, Long> {
    var diffMills = end.time - System.currentTimeMillis()
    if (diffMills <= 0) return Triple(0, 0, 0)

    // Calculate the days, hours and minutes
    val days = TimeUnit.DAYS.convert(diffMills, TimeUnit.MILLISECONDS)
    if (days != 0L) diffMills %= TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS)

    val hours = TimeUnit.HOURS.convert(diffMills, TimeUnit.MILLISECONDS)
    if (hours != 0L) diffMills %= TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS)

    val mins = TimeUnit.MINUTES.convert(diffMills, TimeUnit.MILLISECONDS)

    return Triple(days, hours, mins)
}
