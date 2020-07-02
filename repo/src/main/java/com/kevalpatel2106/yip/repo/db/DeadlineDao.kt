package com.kevalpatel2106.yip.repo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
internal interface DeadlineDao {

    @Query(
        "SELECT COUNT(${DeadlineTableInfo.ID}) " +
                "FROM ${DeadlineTableInfo.TABLE_NAME} WHERE ${DeadlineTableInfo.ID} = :deadlineId"
    )
    fun getCount(deadlineId: Long): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deadline: DeadlineDto): Long

    @Query("SELECT * FROM ${DeadlineTableInfo.TABLE_NAME}")
    fun observeAll(): Flowable<List<DeadlineDto>>

    @Query("SELECT * FROM ${DeadlineTableInfo.TABLE_NAME} WHERE ${DeadlineTableInfo.ID} = :deadlineId")
    fun observe(deadlineId: Long): Flowable<DeadlineDto>

    @Query("DELETE FROM ${DeadlineTableInfo.TABLE_NAME} WHERE ${DeadlineTableInfo.ID} = :deadlineId")
    fun delete(deadlineId: Long)
}
