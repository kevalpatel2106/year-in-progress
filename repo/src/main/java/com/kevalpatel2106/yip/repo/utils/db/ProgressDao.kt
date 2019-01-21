package com.kevalpatel2106.yip.repo.utils.db

import androidx.room.*
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
internal interface ProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(progress: ProgressDto): Long

    @Query("SELECT * FROM progresses")
    fun observeAll(): Flowable<List<ProgressDto>>

    @Query("SELECT * FROM progresses WHERE id = :progressId")
    fun observe(progressId: Long): Flowable<ProgressDto>

    @Query("DELETE FROM progresses WHERE id = :progressId")
    fun delete(progressId: Long)
}