package com.kevalpatel2106.yip.repo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
internal interface ProgressDao {

    @Query(
        "SELECT COUNT(${ProgressTableInfo.ID}) " +
                "FROM ${ProgressTableInfo.TABLE_NAME} WHERE ${ProgressTableInfo.ID} = :progressId"
    )
    fun getCount(progressId: Long): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(progress: ProgressDto): Long

    @Query("SELECT * FROM ${ProgressTableInfo.TABLE_NAME}")
    fun observeAll(): Flowable<List<ProgressDto>>

    @Query("SELECT * FROM ${ProgressTableInfo.TABLE_NAME} WHERE ${ProgressTableInfo.ID} = :progressId")
    fun observe(progressId: Long): Flowable<ProgressDto>

    @Query("DELETE FROM ${ProgressTableInfo.TABLE_NAME} WHERE ${ProgressTableInfo.ID} = :progressId")
    fun delete(progressId: Long)
}