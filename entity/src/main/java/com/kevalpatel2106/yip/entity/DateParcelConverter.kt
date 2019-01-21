package com.kevalpatel2106.yip.entity
import android.os.Parcel
import kotlinx.android.parcel.Parceler
import java.util.*

internal object DateParcelConverter : Parceler<Date?> {
    override fun Date?.write(parcel: Parcel, flags: Int) {
        parcel.writeLong(this?.time ?: 0L)
    }

    override fun create(parcel: Parcel): Date? = with(parcel.readLong()) {
        if (this == 0L) null else Date(this)
    }
}