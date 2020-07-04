package com.kevalpatel2106.yip.repo.utils.dateFormatter

import android.app.Application
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.utils.sharedPrefs.SharedPrefsProvider
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.Calendar
import java.util.Date

@RunWith(Parameterized::class)
class DateFormatterTest(
    private val dateFormat: String,
    private val timeFormat: String,
    private val date: Date,
    private val formatWithTime: String,
    private val formatDateOnly: String
) {

    companion object {
        private val calender = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2019)
            set(Calendar.MONTH, 11)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 13)
            set(Calendar.MINUTE, 10)
            set(Calendar.SECOND, 10)
        }
        private val date = Date(calender.timeInMillis)

        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                // Time: hh:mm a
                arrayOf(
                    "dd-MMM-yyyy", "hh:mm a",
                    date, "01-Dec-2019 01:10 PM", "01-Dec-2019"
                ),
                arrayOf(
                    "dd-MM-yyyy", "hh:mm a",
                    date, "01-12-2019 01:10 PM", "01-12-2019"
                ),
                arrayOf(
                    "dd MMM yyyy", "hh:mm a",
                    date, "01 Dec 2019 01:10 PM", "01 Dec 2019"
                ),
                arrayOf(
                    "MM-dd-yyyy", "hh:mm a",
                    date, "12-01-2019 01:10 PM", "12-01-2019"
                ),
                arrayOf(
                    "MMM dd yyyy", "hh:mm a",
                    date, "Dec 01 2019 01:10 PM", "Dec 01 2019"
                ),
                arrayOf(
                    "MMM dd,yyyy", "hh:mm a",
                    date, "Dec 01,2019 01:10 PM", "Dec 01,2019"
                ),

                // Time: HH:mm
                arrayOf(
                    "dd-MMM-yyyy", "HH:mm",
                    date, "01-Dec-2019 13:10", "01-Dec-2019"
                ),
                arrayOf(
                    "dd-MM-yyyy", "HH:mm",
                    date, "01-12-2019 13:10", "01-12-2019"
                ),
                arrayOf(
                    "dd MMM yyyy", "HH:mm",
                    date, "01 Dec 2019 13:10", "01 Dec 2019"
                ),
                arrayOf(
                    "MM-dd-yyyy", "HH:mm",
                    date, "12-01-2019 13:10", "12-01-2019"
                ),
                arrayOf(
                    "MMM dd yyyy", "HH:mm",
                    date, "Dec 01 2019 13:10", "Dec 01 2019"
                ),
                arrayOf(
                    "MMM dd,yyyy", "HH:mm",
                    date, "Dec 01,2019 13:10", "Dec 01,2019"
                )
            )
        }
    }

    private val keyDateFormat = "pref_key_date_format"
    private val keyTimeFormat = "pref_key_time_format"

    @Mock
    lateinit var sharedPrefsProvider: SharedPrefsProvider

    @Mock
    lateinit var application: Application

    private lateinit var dateFormatter: DateFormatter

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)

        whenever(application.getString(R.string.pref_key_date_format))
            .thenReturn(keyDateFormat)
        whenever(application.getString(R.string.pref_key_time_format))
            .thenReturn(keyTimeFormat)
        whenever(application.getString(R.string.dd_MMM_yyyy)).thenReturn(dateFormat)
        whenever(application.getString(R.string.hh_mm_a)).thenReturn(timeFormat)

        whenever(sharedPrefsProvider.observeStringFromPreference(keyDateFormat, dateFormat))
            .thenReturn(Observable.just(dateFormat))
        whenever(sharedPrefsProvider.observeStringFromPreference(keyTimeFormat, timeFormat))
            .thenReturn(Observable.just(timeFormat))

        dateFormatter = DateFormatterImpl(application, sharedPrefsProvider)
    }

    @Test
    fun checkDateTimeFormat() {
        assertEquals(formatWithTime, dateFormatter.format(date))
    }

    @Test
    fun checkDateFormat() {
        assertEquals(formatDateOnly, dateFormatter.formatDateOnly(date))
    }

}
