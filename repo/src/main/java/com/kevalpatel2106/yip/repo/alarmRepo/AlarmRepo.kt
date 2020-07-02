package com.kevalpatel2106.yip.repo.alarmRepo

import android.content.BroadcastReceiver

interface AlarmRepo {
    fun <T : BroadcastReceiver> updateAlarms(triggerReceiver: Class<T>)

    companion object {
        const val ARG_DEADLINE_ID = "arg_deadline_id"
    }
}
