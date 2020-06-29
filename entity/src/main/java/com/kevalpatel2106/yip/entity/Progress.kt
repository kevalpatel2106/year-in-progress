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
    val start: Date,
    val end: Date,
    val color: ProgressColor,
    val notificationPercent: List<Float>,
    val percent: Float = 0f
) : Parcelable
