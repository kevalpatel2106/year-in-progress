package com.kevalpatel2106.yip.repo.utils

import java.util.*

internal fun Calendar.getFirstDayOfQuarter(): Date {
    set(Calendar.MONTH, get(Calendar.MONTH) / 3 * 3)
    set(Calendar.DAY_OF_MONTH, 1)
    return Date(timeInMillis)
}

internal fun Calendar.getLastDayOfQuarter(): Date {
    set(Calendar.MONTH, get(Calendar.MONTH) / 3 * 3 + 2)
    set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
    return Date(timeInMillis)
}

internal fun Calendar.startOfTheDay(): Date {
    setToDayMin()
    return Date(timeInMillis)
}

internal fun Calendar.endOfTheDay(): Date {
    setToDayMax()
    return Date(timeInMillis)
}

internal fun Calendar.startOfTheMonth(): Date {
    set(Calendar.DAY_OF_MONTH, 1)
    setToDayMin()
    return Date(timeInMillis)
}

internal fun Calendar.endOfTheMonth(): Date {
    startOfTheMonth()
    add(Calendar.MONTH, 1)
    return Date(timeInMillis)
}

internal fun Calendar.startOfTheWeek(): Date {
    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
    setToDayMin()
    return Date(timeInMillis)
}

internal fun Calendar.endOfTheWeek(): Date {
    startOfTheWeek()
    add(Calendar.WEEK_OF_YEAR, 1)
    return Date(timeInMillis)
}

internal fun Calendar.startOfTheYear(): Date {
    set(get(Calendar.YEAR), 0, 1)
    setToDayMin()
    return Date(timeInMillis)
}

internal fun Calendar.endOfTheYear(): Date {
    startOfTheYear()
    add(Calendar.YEAR, 1)
    return Date(timeInMillis)
}

private fun Calendar.setToDayMin() {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

private fun Calendar.setToDayMax() {
    set(Calendar.HOUR_OF_DAY, 23)
    set(Calendar.MINUTE, 59)
    set(Calendar.SECOND, 59)
    set(Calendar.MILLISECOND, 999)
}

internal fun calculatePercent(now: Date, start: Date, end: Date): Float {
    val percent = (now.time - start.time) * 100 / (end.time - start.time).toFloat()
    return when {
        percent > 100f -> 100f
        percent < 0f -> 0f
        else -> percent
    }
}