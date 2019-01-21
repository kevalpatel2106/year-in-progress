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
    val order: Int,
    val prebuiltProgress: PrebuiltProgress,
    val title: String,

    var start: Date,

    var end: Date,

    @ColorInt
    var color: ProgressColor = ProgressColor.COLOR_BLUE,

    val isEnabled: Boolean = true
) : Parcelable {

    fun percent(now: Date) = (now.time - start.time) * 100 / (end.time - start.time).toFloat()
}