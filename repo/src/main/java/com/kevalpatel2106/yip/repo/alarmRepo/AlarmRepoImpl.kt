package com.kevalpatel2106.yip.repo.alarmRepo

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.VisibleForTesting
import androidx.core.app.AlarmManagerCompat
import androidx.core.os.bundleOf
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
import com.kevalpatel2106.yip.repo.timeProvider.TimeProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.functions.BiFunction
import timber.log.Timber
import java.util.Date
import kotlin.math.roundToLong

internal class AlarmRepoImpl(
    private val alarmManager: AlarmManager,
    @ApplicationContext private val application: Context,
    private val timeProvider: TimeProvider,
    private val yipDatabase: YipDatabase
) : AlarmRepo {

    @SuppressLint("CheckResult")
    override fun <T : BroadcastReceiver> updateAlarms(triggerReceiver: Class<T>) {
        yipDatabase.getDeviceDao()
            .observeAll()
            .firstOrError()
            .zipWith(
                timeProvider.nowAsync(),
                BiFunction { dtos: List<DeadlineDto>, date: Date -> dtos to date }
            )
            .subscribe({ (dtos, now) ->
                dtos.forEach { dto -> updateAlarmsForDeadline(dto, triggerReceiver, now.time) }
            }, {
                Timber.e(it)
            })
    }

    private fun <T : BroadcastReceiver> updateAlarmsForDeadline(
        deadline: DeadlineDto,
        triggerReceiver: Class<T>,
        nowMills: Long
    ) {
        deadline.notifications
            .map { triggerMills(deadline.start.time, deadline.end.time, it) }
            .filter { triggerMills -> triggerMills > nowMills }
            .forEach { triggerMills ->
                AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.RTC_WAKEUP,
                    triggerMills,
                    getPendingIntent(
                        application = application,
                        alarmTime = triggerMills,
                        deadlineId = deadline.id,
                        triggerClass = triggerReceiver
                    )
                )
            }
    }

    private fun <T : BroadcastReceiver> getPendingIntent(
        application: Context,
        alarmTime: Long,
        deadlineId: Long,
        triggerClass: Class<T>
    ): PendingIntent {
        val intent = Intent(application, triggerClass)
            .apply { putExtras(bundleOf(AlarmRepo.ARG_DEADLINE_ID to deadlineId)) }
        return PendingIntent.getBroadcast(
            application,
            alarmTime.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @VisibleForTesting
    fun triggerMills(startMills: Long, endMills: Long, triggerPercent: Float): Long {
        return if (startMills < 0 || endMills < 0) {
            throw IllegalArgumentException("Start mills or end mills is negative.")
        } else if (startMills > endMills) {
            throw IllegalArgumentException("Start mills greater than end mills.")
        } else {
            startMills + ((endMills - startMills) * (triggerPercent / 100)).roundToLong()
        }
    }
}
