package com.kevalpatel2106.yip.repo.progressesRepo

import android.app.Application
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import com.kevalpatel2106.yip.repo.dto.modifyPrebuiltProgress
import com.kevalpatel2106.yip.repo.dto.toEntity
import com.kevalpatel2106.yip.repo.providers.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.providers.TimeProvider
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import java.util.Date

internal class ProgressRepoImpl(
    private val application: Application,
    private val db: YipDatabase,
    private val timeProvider: TimeProvider,
    private val sharedPrefsProvider: SharedPrefsProvider
) : ProgressRepo {
    private val orderKeyAtoZ by lazy { application.getString(R.string.order_title_a_to_z) }
    private val orderKeyZtoA by lazy { application.getString(R.string.order_title_z_to_a) }
    private val orderKeyEndingTimeAsc by lazy { application.getString(R.string.order_end_time_ascending) }
    private val orderKeyEndingTimeDesc by lazy { application.getString(R.string.order_end_time_descending) }

    override fun observeAllProgress(): Flowable<List<Progress>> {
        val progressObserver = db.getDeviceDao().observeAll()
        val progressOrderObserver = sharedPrefsProvider
            .observeStringFromPreference(
                application.getString(R.string.pref_key_order),
                orderKeyAtoZ
            )
            .toFlowable(BackpressureStrategy.DROP)

        return Flowable.combineLatest(
            progressObserver,
            timeProvider.minuteObserver(),
            progressOrderObserver,
            Function3<List<ProgressDto>, Date, String, Triple<List<ProgressDto>, Date, String>> { list, now, order ->
                Triple(list, now, order)
            }
        ).map { (progressesDto, now, sortOrder) ->
            val progresses = progressesDto.map { progress ->
                progress.modifyPrebuiltProgress(now).toEntity(now)
            }

            return@map when (sortOrder) {
                orderKeyAtoZ -> progresses.sortedBy { it.title }
                orderKeyZtoA -> progresses.sortedByDescending { it.title }
                orderKeyEndingTimeAsc -> progresses.sortedBy { it.end.time }
                orderKeyEndingTimeDesc -> progresses.sortedByDescending { it.end.time }
                else -> throw IllegalArgumentException("Invalid sort order: $sortOrder")
            }
        }.subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
    }

    override fun observeProgress(progressId: Long): Flowable<Progress> {
        return Flowable.combineLatest(
            db.getDeviceDao().observe(progressId),
            timeProvider.minuteObserver(),
            BiFunction<ProgressDto, Date, Pair<ProgressDto, Date>> { progressDto, now ->
                progressDto to now
            }
        ).map { (progressDto, now) ->
            progressDto.modifyPrebuiltProgress(now).toEntity(now)
        }.subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
    }

    override fun isProgressExist(progressId: Long): Single<Boolean> {
        return db.getDeviceDao()
            .getCount(progressId)
            .map { it > 0 }
            .subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
    }

    override fun deleteProgress(progressId: Long): Completable {
        return Completable.fromCallable { db.getDeviceDao().delete(progressId) }
            .subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
    }

    override fun addUpdateProgress(
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
            timeProvider.nowAsync(),
            BiFunction { dto: ProgressDto, now: Date -> dto to now }
        ).map { (dto, now) ->
            dto.toEntity(now)
        }.subscribeOn(RxSchedulers.database)
            .observeOn(RxSchedulers.main)
    }
}
