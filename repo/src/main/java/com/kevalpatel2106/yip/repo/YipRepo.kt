package com.kevalpatel2106.yip.repo

import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import com.kevalpatel2106.yip.repo.dto.modifyPrebuiltProgress
import com.kevalpatel2106.yip.repo.dto.toEntity
import com.kevalpatel2106.yip.repo.utils.NtpProvider
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import com.kevalpatel2106.yip.repo.utils.db.YipDatabase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class YipRepo @Inject constructor(private val db: YipDatabase, private val ntpProvider: NtpProvider) {

    fun observeAllProgress(): Flowable<List<Progress>> {
        return Flowable.combineLatest(
                db.getDeviceDao().observeAll(),
                Flowable.interval(0, 1, TimeUnit.MINUTES, RxSchedulers.compute),
                BiFunction<List<ProgressDto>, Long, List<ProgressDto>> { t1, _ -> t1 }
        ).map { progresses ->
            return@map progresses.map { progress -> progress.modifyPrebuiltProgress(ntpProvider).toEntity() }
        }.subscribeOn(RxSchedulers.database)
                .observeOn(RxSchedulers.main)
    }

    fun observeProgress(progressId: Long): Flowable<Progress> {
        return Flowable.combineLatest(
                db.getDeviceDao().observe(progressId),
                Flowable.interval(0, 1, TimeUnit.MINUTES, RxSchedulers.compute),
                BiFunction<ProgressDto, Long, ProgressDto> { t1, _ -> t1 }
        ).map { progress ->
            progress.modifyPrebuiltProgress(ntpProvider).toEntity()
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
            progressTypeType: ProgressType
    ): Single<Progress> {
        return Single.create<ProgressDto> { emitter ->
            val dto = ProgressDto(
                    id = processId,
                    color = color,
                    end = endTime,
                    isEnabled = true,
                    order = 20,
                    progressType = progressTypeType,
                    start = startTime,
                    title = title
            )
            dto.id = db.getDeviceDao().insert(dto)
            emitter.onSuccess(dto)
        }.map {
            it.toEntity()
        }.subscribeOn(RxSchedulers.database)
                .observeOn(RxSchedulers.main)
    }
}