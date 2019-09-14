package com.kevalpatel2106.yip.core

import java.util.Calendar
import java.util.Date

private const val MAX_HOUR = 23
private const val MAX_MINUTES = 59
private const val MAX_SECONDS = 59
private const val MAX_MILLISECONDS = 999

fun Date.setToDayMin() {
    val cal = Calendar.getInstance()
    cal.timeInMillis = time

    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)

    time = cal.timeInMillis
}

fun Date.setToDayMax() {
    val cal = Calendar.getInstance()
    cal.timeInMillis = time

    cal.set(Calendar.HOUR_OF_DAY, MAX_HOUR)
    cal.set(Calendar.MINUTE, MAX_MINUTES)
    cal.set(Calendar.SECOND, MAX_SECONDS)
    cal.set(Calendar.MILLISECOND, MAX_MILLISECONDS)

    time = cal.timeInMillis
}
