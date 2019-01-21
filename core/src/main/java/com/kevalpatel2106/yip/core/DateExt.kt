package com.kevalpatel2106.yip.core

import java.util.*


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

    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    cal.set(Calendar.SECOND, 59)
    cal.set(Calendar.MILLISECOND, 999)

    time = cal.timeInMillis
}