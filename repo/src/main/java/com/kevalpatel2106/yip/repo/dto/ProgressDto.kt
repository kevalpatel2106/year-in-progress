package com.kevalpatel2106.yip.repo.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import com.kevalpatel2106.yip.repo.db.ProgressTableInfo
import java.util.Date

@Entity(tableName = ProgressTableInfo.TABLE_NAME)
internal data class ProgressDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ProgressTableInfo.ID)
    var id: Long = 0,

    @ColumnInfo(name = ProgressTableInfo.TYPE)
    val progressType: ProgressType,

    @ColumnInfo(name = ProgressTableInfo.TITLE)
    val title: String,

    @ColumnInfo(name = ProgressTableInfo.START_TIME)
    var start: Date,

    @ColumnInfo(name = ProgressTableInfo.END_TIME)
    var end: Date,

    @ColumnInfo(name = ProgressTableInfo.COLOR)
    var color: ProgressColor = ProgressColor.COLOR_BLUE,

    @ColumnInfo(name = ProgressTableInfo.NOTIFICATIONS_PERCENTS)
    val notifications: List<Float> = listOf()
)
