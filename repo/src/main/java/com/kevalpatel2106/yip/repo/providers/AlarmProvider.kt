package com.kevalpatel2106.yip.repo.providers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Build
import androidx.core.os.bundleOf
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import io.reactivex.functions.BiFunction
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import kotlin.math.roundToLong

class AlarmProvider @Inject internal constructor(
    private val alarmManager: AlarmManager,
    private val application: Application,
    private val timeProvider: TimeProvider,
    private val yipDatabase: YipDatabase
) {

    @SuppressLint("CheckResult")
    fun <T : BroadcastReceiver> updateAlarms(triggerReceiver: Class<T>) {
        yipDatabase.getDeviceDao()
            .observeAll()
            .firstOrError()
            .zipWith(
                timeProvider.nowAsync(),
                BiFunction { dtos: List<ProgressDto>, date: Date -> dtos to date }
            )
            .subscribe({ (dtos, now) ->
                dtos.forEach { dto -> updateAlarmsForProgress(dto, triggerReceiver, now.time) }
            }, {
                Timber.e(it)
            })
    }


    private fun <T : BroadcastReceiver> updateAlarmsForProgress(
        progress: ProgressDto,
        triggerReceiver: Class<T>,
        nowMills: Long
    ) {
        progress.notifications
            .map { getTriggerMills(progress.start.time, progress.end.time, it) }
            .filter { triggerMills -> triggerMills > nowMills }
            .forEach { triggerMills ->
                alarmManager.setExactCompat(
                    AlarmManager.RTC_WAKEUP,
                    triggerMills,
                    getPendingIntent(
                        application = application,
                        alarmTime = triggerMills,
                        progressId = progress.id,
                        triggerClass = triggerReceiver
                    )
                )
            }
    }

    private fun <T : BroadcastReceiver> getPendingIntent(
        application: Application,
        alarmTime: Long,
        progressId: Long,
        triggerClass: Class<T>
    ): PendingIntent {
        return PendingIntent.getBroadcast(
            application,
            alarmTime.toInt(),
            Intent(application, triggerClass).apply {
                putExtras(bundleOf(ARG_PROGRESS_ID to progressId))
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    companion object {
        private fun getTriggerMills(startMills: Long, endMills: Long, triggerPercent: Float): Long {
            return startMills + ((endMills - startMills) * (triggerPercent / 100)).roundToLong()
        }

        private fun AlarmManager.setExactCompat(
            type: Int,
            triggerAtMillis: Long,
            operation: PendingIntent
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setExactAndAllowWhileIdle(type, triggerAtMillis, operation)
            } else {
                setExact(type, triggerAtMillis, operation)
            }
        }

        const val ARG_PROGRESS_ID = "arg_progress_id"
    }
}
