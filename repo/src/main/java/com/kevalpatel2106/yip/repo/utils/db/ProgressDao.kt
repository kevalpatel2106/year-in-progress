package com.kevalpatel2106.yip.repo.utils.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import io.reactivex.Flowable

@Dao
internal interface ProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(progress: ProgressDto): Long

    @Query("SELECT * FROM ${ProgressTableInfo.TABLE_NAME}")
    fun observeAll(): Flowable<List<ProgressDto>>

    @Query("SELECT * FROM ${ProgressTableInfo.TABLE_NAME} WHERE ${ProgressTableInfo.ID} = :progressId")
    fun observe(progressId: Long): Flowable<ProgressDto>

    @Query("DELETE FROM ${ProgressTableInfo.TABLE_NAME} WHERE ${ProgressTableInfo.ID} = :progressId")
    fun delete(progressId: Long)
}