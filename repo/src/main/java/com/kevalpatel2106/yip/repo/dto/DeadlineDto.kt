package com.kevalpatel2106.yip.repo.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.db.DeadlineTableInfo
import java.util.Date

@Entity(tableName = DeadlineTableInfo.TABLE_NAME)
internal data class DeadlineDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DeadlineTableInfo.ID)
    var id: Long = 0,

    @ColumnInfo(name = DeadlineTableInfo.TYPE)
    val type: DeadlineType,

    @ColumnInfo(name = DeadlineTableInfo.TITLE)
    val title: String,

    @ColumnInfo(name = DeadlineTableInfo.START_TIME)
    var start: Date,

    @ColumnInfo(name = DeadlineTableInfo.END_TIME)
    var end: Date,

    @ColumnInfo(name = DeadlineTableInfo.COLOR)
    var color: DeadlineColor = DeadlineColor.COLOR_BLUE,

    @ColumnInfo(name = DeadlineTableInfo.NOTIFICATIONS_PERCENTS)
    val notifications: List<Float> = listOf()
)
