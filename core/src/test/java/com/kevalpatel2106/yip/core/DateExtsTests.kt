package com.kevalpatel2106.yip.core

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class DateExtsTests {

    @Test
    fun checkDayMinDate() {
        val input = Date(2019, 1, 1, 1, 1, 1)
        input.setToDayMin()
        val minDate = Date(2019, 1, 1)
        Assert.assertEquals(minDate.time, input.time)
    }

    @Test
    fun checkDayMaxDate() {
        val input = Date(2019, 1, 1, 1, 1, 1)
        input.setToDayMax()
        val maxDate = Date(2019, 1, 1, 23, 59, 59).apply { time += 999 }

        Assert.assertEquals(maxDate.time, input.time)
    }
}