package com.kevalpatel2106.yip.entity

import android.os.Parcelable
import com.kevalpatel2106.testutils.testParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

@Parcelize
@TypeParceler<Date, DateParcelConverter>()
private data class DateParcel(val date: Date?) : Parcelable

@RunWith(RobolectricTestRunner::class)
class DateParcelConverterTest {
    private val testDate = Date().apply { time = System.currentTimeMillis() }

    @Test
    @Throws(Exception::class)
    fun testWriteParcel_Date() {
        val dateParcel = DateParcel(testDate)
        dateParcel.testParcel().apply { Assert.assertEquals(testDate.time, this.date?.time) }
    }

    @Test
    @Throws(Exception::class)
    fun testWriteParcel_NullDate() {
        val dateParcel = DateParcel(null)
        dateParcel.testParcel().apply { Assert.assertNull(this.date) }
    }
}