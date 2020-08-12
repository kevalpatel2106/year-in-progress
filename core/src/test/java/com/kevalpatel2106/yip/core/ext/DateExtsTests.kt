package com.kevalpatel2106.yip.core.ext

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Date

@Suppress("DEPRECATION")
@RunWith(Parameterized::class)
class DateExtsTests(
    private val inputDate: Date,
    private val minDate: Date,
    private val maxDate: Date
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<Date>> {
            return arrayListOf(
                arrayOf(
                    Date(2019, 1, 1, 1, 1, 1),
                    Date(2019, 1, 1),
                    Date(2019, 1, 1, 23, 59, 59).apply { time += 999 }
                ),
                arrayOf(
                    Date(2000, 1, 1, 23, 59, 59),
                    Date(2000, 1, 1),
                    Date(2000, 1, 1, 23, 59, 59).apply { time += 999 }
                ),
                arrayOf(
                    Date(2199, 12, 31, 0, 0, 0),
                    Date(2199, 12, 31),
                    Date(2219, 12, 31, 23, 59, 59).apply { time += 999 }
                )
            )
        }
    }

    @Test
    fun checkDayMinDate() {
        inputDate.setToDayMin()
        Assert.assertEquals(minDate.time, inputDate.time)
    }

    @Test
    fun checkDayMaxDate() {
        inputDate.setToDayMax()
        Assert.assertEquals(maxDate.time, maxDate.time)
    }
}
