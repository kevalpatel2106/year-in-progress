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
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class YipRepo @Inject internal constructor(
        private val application: Application,
        private val db: YipDatabase,
        private val ntpProvider: NtpProvider,
        private val sharedPrefsProvider: SharedPrefsProvider
) {

    fun observeAllProgress(): Flowable<List<Progress>> {
        return Flowable.combineLatest(
                db.getDeviceDao().observeAll(),
                Flowable.interval(0, 1, TimeUnit.MINUTES, RxSchedulers.compute),
                sharedPrefsProvider.observeStringFromPreference(
                        application.getString(R.string.pref_key_order),
                        application.getString(R.string.order_title_a_to_z)
                ).toFlowable(BackpressureStrategy.DROP),
                Function3<List<ProgressDto>, Long, String, Pair<List<ProgressDto>, String>> { list, _, order ->
                    list to order
                }
        ).zipWith(
                ntpProvider.nowAsync().toFlowable(),
                BiFunction { item: Pair<List<ProgressDto>, String>, date: Date ->
                    Triple(item.first, item.second, date)
                }
        ).map { (progressesDto, sortOrder, now) ->
            val progresses = progressesDto.map { progress ->
                progress.modifyPrebuiltProgress(now.time).toEntity()
            }

            return@map when (sortOrder) {
                application.getString(R.string.order_title_a_to_z) -> progresses.sortedBy { it.title }
                application.getString(R.string.order_title_z_to_a) -> progresses.sortedByDescending { it.title }
                application.getString(R.string.order_end_time_ascending) -> progresses.sortedBy { it.end.time }
                application.getString(R.string.order_end_time_descending) -> progresses.sortedByDescending { it.end.time }
                else -> throw IllegalArgumentException("Invalid sort order: $sortOrder")
            }
        }.subscribeOn(RxSchedulers.database)
                .observeOn(RxSchedulers.main)
    }

    fun observeProgress(progressId: Long): Flowable<Progress> {
        return Flowable.combineLatest(
                db.getDeviceDao().observe(progressId),
                Flowable.interval(0, 1, TimeUnit.MINUTES, RxSchedulers.compute),
                BiFunction<ProgressDto, Long, ProgressDto> { t1, _ -> t1 }
        ).zipWith(
                ntpProvider.nowAsync().toFlowable(),
                BiFunction { item: ProgressDto, date: Date -> item to date }
        ).map { (progressDto, now) ->
            progressDto.modifyPrebuiltProgress(now.time)
        }.map { dto ->
            dto.toEntity()
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
        }.map {
            it.toEntity()
        }.subscribeOn(RxSchedulers.database)
                .observeOn(RxSchedulers.main)
    }

    private fun getSortingOrder(): String {
        return sharedPrefsProvider.getStringFromPreference(application.getString(R.string.pref_key_order))
            ?: application.getString(R.string.order_title_a_to_z)
    }
}