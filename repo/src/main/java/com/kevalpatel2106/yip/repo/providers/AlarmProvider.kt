package com.kevalpatel2106.yip.repo.providers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import timber.log.Timber
import javax.inject.Inject

class AlarmProvider @Inject internal constructor(
        private val application: Application,
        private val ntpProvider: NtpProvider,
        private val yipDatabase: YipDatabase
) {
    private val alarmManager: AlarmManager by lazy {
        application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @SuppressLint("CheckResult")
    fun <T> updateAlarms(triggerReceiver: Class<T>) where T : BroadcastReceiver {
        yipDatabase.getDeviceDao()
                .observeAll()
                .firstElement()
                .subscribe({ dtos ->
                    dtos.forEach { updateAlarmsForProgress(it, triggerReceiver) }
                }, {
                    Timber.e(it)
                })
    }

    private fun <T> updateAlarmsForProgress(progress: ProgressDto, triggerReceiver: Class<T>) where T : BroadcastReceiver {
        val nowMills = ntpProvider.now().time
        progress.notifications
                .filter { triggerMills -> triggerMills > nowMills }
                .forEach { triggerMills ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                triggerMills,
                                getPendingIntent(triggerMills, triggerReceiver)
                        )
                    } else {
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                triggerMills,
                                getPendingIntent(triggerMills, triggerReceiver)
                        )
                    }
                }
    }

    private fun <T> getPendingIntent(alarmTime: Long, triggerClass: Class<T>): PendingIntent where T : BroadcastReceiver {
        return PendingIntent.getBroadcast(
                application,
                alarmTime.toInt(),
                Intent(application, triggerClass),
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}