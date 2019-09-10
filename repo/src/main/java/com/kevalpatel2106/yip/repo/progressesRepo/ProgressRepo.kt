@file:Suppress("TooManyFunctions")

package com.kevalpatel2106.yip.repo.progressesRepo

import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.Date

interface ProgressRepo {
    fun observeAllProgress(): Flowable<List<Progress>>
    fun observeProgress(progressId: Long): Flowable<Progress>
    fun isProgressExist(progressId: Long): Single<Boolean>
    fun deleteProgress(progressId: Long): Completable
    fun addUpdateProgress(
        processId: Long,
        title: String,
        color: ProgressColor,
        startTime: Date,
        endTime: Date,
        progressTypeType: ProgressType,
        notifications: List<Float>
    ): Single<Progress>
}
