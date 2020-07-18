@file:Suppress("TooManyFunctions")

package com.kevalpatel2106.yip.repo.deadlineRepo

import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.Date

interface DeadlineRepo {
    fun observeAllDeadlines(): Flowable<List<Deadline>>
    fun observeDeadline(deadlineId: Long): Flowable<Deadline>
    fun isDeadlineExist(deadlineId: Long): Single<Boolean>
    fun deleteDeadline(deadlineId: Long): Completable
    fun addUpdateDeadline(
        deadlineId: Long,
        title: String,
        description: String?,
        color: DeadlineColor,
        startTime: Date,
        endTime: Date,
        type: DeadlineType,
        notifications: List<Float>
    ): Single<Deadline>
}
