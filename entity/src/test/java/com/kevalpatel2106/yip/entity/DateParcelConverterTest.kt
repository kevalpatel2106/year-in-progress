package com.kevalpatel2106.yip.entity

import android.os.Parcelable
import com.kevalpatel2106.testutils.testParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

@Parcelize
@TypeParceler<Date, DateParcelConverter>()
private data class DateParcel(val date: Date) : Parcelable

@RunWith(RobolectricTestRunner::class)
class DateParcelConverterTest {

    private val testDate = Date().apply { time = System.currentTimeMillis() }

    private lateinit var dateParcel: DateParcel

    @Before
    fun setUp() {
        dateParcel = DateParcel(testDate)
    }

    @Test
    @Throws(Exception::class)
    fun testWriteParcel() {
        dateParcel.testParcel().apply { Assert.assertEquals(testDate.time, this.date.time) }
    }
}