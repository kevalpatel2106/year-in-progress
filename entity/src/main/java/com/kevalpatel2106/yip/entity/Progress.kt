package com.kevalpatel2106.yip.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import java.util.Date

@Parcelize
@TypeParceler<Date, DateParcelConverter>()
data class Progress(
    val id: Long,

    val progressType: ProgressType,
    val title: String,

    var start: Date,
    var end: Date,

    var color: ProgressColor = ProgressColor.COLOR_BLUE,

    var notificationPercent: List<Float>,

    var percent: Float = 0f
) : Parcelable {

    override fun equals(other: Any?): Boolean =
        (other as? Progress)?.id == id && (other as? Progress)?.percent == percent

    override fun hashCode(): Int = id.hashCode() + percent.toInt()
}
