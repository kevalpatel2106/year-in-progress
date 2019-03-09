package com.kevalpatel2106.yip.repo

import android.app.Application
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import com.kevalpatel2106.yip.repo.dto.modifyPrebuiltProgress
import com.kevalpatel2106.yip.repo.dto.toEntity
import com.kevalpatel2106.yip.repo.providers.NtpProvider
import com.kevalpatel2106.yip.repo.providers.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import java.util.Date
import javax.inject.Inject

class YipRepo @Inject internal constructor(
    private val application: Application,
    private val db: YipDatabase,
    private val ntpProvider: NtpProvider,
    private val sharedPrefsProvider: SharedPrefsProvider
) {

    fun observeAllProgress(): Flowable<List<Progress>> {
        return Flowable.combineLatest(
            db.getDeviceDao().observeAll().map { it },
            ntpProvider.nowAsyncInterval(),
            sharedPrefsProvider.observeStringFromPreference(
                application.getString(R.string.pref_key_order),
                application.getString(R.string.order_title_a_to_z)
            ).toFlowable(BackpressureStrategy.DROP),
            Function3<List<ProgressDto>, Date, String, Triple<List<ProgressDto>, String, Date>> { list, now, order ->
                Triple(list, order, now)
            }
        ).map { (progressesDto, sortOrder, now) ->
            val progresses = progressesDto.map { progress ->
                progress.modifyPrebuiltProgress(now.time).toEntity(now)
            }

            return@map when (sortOrder) {
                application.getString(R.string.order_title_a_to_z) -> {
                    progresses.sortedBy { it.title }
                }
                application.getString(R.string.order_title_z_to_a) -> {
                    progresses.sortedByDescending { it.title }
                }
                application.getString(R.string.order_end_time_ascending) -> {
                    progresses.sortedBy { it.end.time }
                }
                application.getString(R.string.order_end_time_descending) -> {
                    progresses.sortedByDescending { it.end.time }
                }
                else -> throw IllegalArgumentException("Invalid sort order: $sortOrder")
            }
        }.subscribeOn(RxSchedulers.database).observeOn(RxSchedulers.main)
    }

    fun observeProgress(progressId: Long): Flowable<Progress> {
        return Flowable.combineLatest(
            db.getDeviceDao().observe(progressId),
            ntpProvider.nowAsyncInterval(),
            BiFunction<ProgressDto, Date, Pair<ProgressDto, Date>> { progressDto, now ->
                progressDto to now
            }
        ).map { (progressDto, now) ->
            progressDto.modifyPrebuiltProgress(now.time).toEntity(now)
        }.subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
    }

    fun deleteProgress(progressId: Long): Completable {
        return Completable.create {
            db.getDeviceDao().delete(progressId)
            it.onComplete()
        }.subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
    }

    fun addUpdateProgress(
        processId: Long,
        title: String,
        color: ProgressColor,
        startTime: Date,
        endTime: Date,
        progressTypeType: ProgressType,
        notifications: List<Float>
    ): Single<Progress> {
        return Single.create<ProgressDto> { emitter ->
            val dto = ProgressDto(
                id = processId,
                color = color,
                end = endTime,
                progressType = progressTypeType,
                start = startTime,
                title = title,
                notifications = notifications
            )
            dto.id = db.getDeviceDao().insert(dto)
            emitter.onSuccess(dto)
        }.zipWith(
            ntpProvider.nowAsync(),
            BiFunction { dto: ProgressDto, now: Date -> dto to now }
        ).map { (dto, now) ->
            dto.toEntity(now)
        }.subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
    }
}