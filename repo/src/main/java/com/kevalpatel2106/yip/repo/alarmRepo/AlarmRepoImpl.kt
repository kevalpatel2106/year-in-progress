package com.kevalpatel2106.yip.repo.alarmRepo

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import androidx.annotation.VisibleForTesting
import androidx.core.app.AlarmManagerCompat
import androidx.core.os.bundleOf
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import com.kevalpatel2106.yip.repo.utils.TimeProvider
import io.reactivex.functions.BiFunction
import timber.log.Timber
import java.util.Date
import kotlin.math.roundToLong

internal class AlarmRepoImpl(
    private val alarmManager: AlarmManager,
    private val application: Application,
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
            .map { triggerMills(progress.start.time, progress.end.time, it) }
            .filter { triggerMills -> triggerMills > nowMills }
            .forEach { triggerMills ->
                AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
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

    companion object {

        private fun <T : BroadcastReceiver> getPendingIntent(
            application: Application,
            alarmTime: Long,
            progressId: Long,
            triggerClass: Class<T>
        ): PendingIntent {
            val intent = Intent(application, triggerClass)
                .apply { putExtras(bundleOf(AlarmRepo.ARG_PROGRESS_ID to progressId)) }
            return PendingIntent.getBroadcast(
                application,
                alarmTime.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        @VisibleForTesting
        internal fun triggerMills(startMills: Long, endMills: Long, triggerPercent: Float): Long {
            return if (startMills < 0 || endMills < 0) {
                throw IllegalArgumentException("Start mills or end mills is negative.")
            } else if (startMills > endMills) {
                throw IllegalArgumentException("Start mills greater than end mills.")
            } else {
                startMills + ((endMills - startMills) * (triggerPercent / 100)).roundToLong()
            }
        }
    }
}
