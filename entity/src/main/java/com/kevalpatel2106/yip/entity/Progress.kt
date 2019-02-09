package com.kevalpatel2106.yip.entity

import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import java.util.*

@Parcelize
@TypeParceler<Date, DateParcelConverter>()
data class Progress(
        val id: Long,

        val progressType: ProgressType,
        val title: String,

        var start: Date,
        var end: Date,

        @ColorInt
        var color: ProgressColor = ProgressColor.COLOR_BLUE,

        var notifications: List<Long>
) : Parcelable {

    fun percent(now: Date): Float {
        val percent = (now.time - start.time) * 100 / (end.time - start.time).toFloat()
        return if (percent > 100f) 100f else percent
    }

    override fun equals(other: Any?): Boolean {
        return (other as? Progress)?.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}