package com.kevalpatel2106.yip.repo.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(Enclosed::class)
class CalenderExtKtTest {

    @RunWith(JUnit4::class)
    class CalenderConversationTest {

        @Test
        fun checkDateToCal() {
            val nowMills = System.currentTimeMillis()
            assertEquals(nowMills, Date(nowMills).toCal().timeInMillis)
        }
    }
}
