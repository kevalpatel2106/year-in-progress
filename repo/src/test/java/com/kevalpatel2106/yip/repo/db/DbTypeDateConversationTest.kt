package com.kevalpatel2106.yip.repo.db

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
class DbTypeDateConversationTest {

    @Test
    fun `given date when date converted to long check long value is unix mills`() {
        // given
        val inputDate = Date(System.currentTimeMillis())

        // when
        val convertedType = DbTypeConverter.toLong(inputDate)

        // check
        assertEquals(inputDate.time, convertedType)
    }

    @Test
    fun `given long value when millisecond converted to date check date`() {
        // given
        val currentMills = System.currentTimeMillis()

        // when
        val convertedType = DbTypeConverter.toDate(currentMills)

        // check
        assertEquals(currentMills, convertedType.time)
    }
}
