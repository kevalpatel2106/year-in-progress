package com.kevalpatel2106.yip.repo.deadlineRepo

import android.content.Context
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.db.DeadlineDao
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
import com.kevalpatel2106.yip.repo.dto.modifyPrebuiltDeadline
import com.kevalpatel2106.yip.repo.dto.toEntity
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.timeProvider.TimeProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import java.util.Date
import javax.inject.Inject

internal class DeadlineRepoImpl @Inject constructor(
    @ApplicationContext private val application: Context,
    private val deadlineDao: DeadlineDao,
    private val timeProvider: TimeProvider,
    private val sharedPrefsProvider: SharedPrefsProvider
) : DeadlineRepo {
    private val orderKeyAtoZ by lazy { application.getString(R.string.order_title_a_to_z) }
    private val orderKeyZtoA by lazy { application.getString(R.string.order_title_z_to_a) }
    private val orderKeyEndingTimeAsc by lazy { application.getString(R.string.order_end_time_ascending) }
    private val orderKeyEndingTimeDesc by lazy { application.getString(R.string.order_end_time_descending) }

    override fun observeAllDeadlines(): Flowable<List<Deadline>> {
        val deadlineObserver = deadlineDao.observeAll()
        val deadlineOrderObserver = sharedPrefsProvider
            .observeStringFromPreference(
                application.getString(R.string.pref_key_order),
                orderKeyAtoZ
            )
            .toFlowable(BackpressureStrategy.DROP)

        return Flowable.combineLatest(
            deadlineObserver,
            timeProvider.minuteObserver(),
            deadlineOrderObserver,
            Function3<List<DeadlineDto>, Date, String, Triple<List<DeadlineDto>, Date, String>> { list, now, order ->
                Triple(list, now, order)
            }
        ).map { (dtos, now, sortOrder) ->
            val deadlines = dtos.map { deadlineDto ->
                deadlineDto.modifyPrebuiltDeadline(now).toEntity(now)
            }

            return@map when (sortOrder) {
                orderKeyAtoZ -> deadlines.sortedBy { it.title }
                orderKeyZtoA -> deadlines.sortedByDescending { it.title }
                orderKeyEndingTimeAsc -> deadlines.sortedBy { it.end.time }
                orderKeyEndingTimeDesc -> deadlines.sortedByDescending { it.end.time }
                else -> throw IllegalArgumentException("Invalid sort order: $sortOrder")
            }
        }
    }

    override fun observeDeadline(deadlineId: Long): Flowable<Deadline> {
        return Flowable.combineLatest(
            deadlineDao.observe(deadlineId),
            timeProvider.minuteObserver(),
            BiFunction<DeadlineDto, Date, Pair<DeadlineDto, Date>> { deadlineDto, now ->
                deadlineDto to now
            }
        ).map { (deadlineDto, now) ->
            deadlineDto.modifyPrebuiltDeadline(now).toEntity(now)
        }
    }

    override fun isDeadlineExist(deadlineId: Long): Single<Boolean> {
        return deadlineDao
            .getCount(deadlineId)
            .map { it > 0 }
            .doOnError {
                it.printStackTrace()
            }
    }

    override fun deleteDeadline(deadlineId: Long): Completable {
        return Completable.fromCallable { deadlineDao.delete(deadlineId) }
    }

    override fun addUpdateDeadline(
        deadlineId: Long,
        title: String,
        description: String?,
        color: DeadlineColor,
        startTime: Date,
        endTime: Date,
        type: DeadlineType,
        notifications: List<Float>
    ): Single<Deadline> {
        return Single.create<DeadlineDto> { emitter ->
            val dto = DeadlineDto(
                id = deadlineId,
                description = description,
                color = color,
                end = endTime,
                type = type,
                start = startTime,
                title = title,
                notifications = notifications
            )
            dto.id = deadlineDao.insert(dto)
            emitter.onSuccess(dto)
        }.zipWith(
            timeProvider.nowAsync(),
            BiFunction { dto: DeadlineDto, now: Date -> dto to now }
        ).map { (dto, now) ->
            dto.toEntity(now)
        }
    }
}
