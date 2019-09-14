package com.kevalpatel2106.yip.repo.alarmRepo

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.Parameterized

@RunWith(Enclosed::class)
class AlarmRepoImplCompanionTest {

    @RunWith(Parameterized::class)
    class CalculateTriggerMillsTest(
        private val startMills: Long,
        private val endMills: Long,
        private val percent: Float,
        private val triggerMills: Long
    ) {

        companion object {

            private val now = System.currentTimeMillis()

            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any?>> {
                return arrayListOf(
                    arrayOf(50, 100, 100, 100),
                    arrayOf(50, 100, 50, 75),
                    arrayOf(50, 100, 0, 50),
                    arrayOf(100, 100, 100, 100),
                    arrayOf(100, 100, 0, 100),
                    arrayOf(now, now + 1000, 25, now + 250),
                    arrayOf(0, 0, 0, 0)
                )
            }
        }

        @Test
        fun checkGetTriggerMills() {
            val mills = AlarmRepoImpl.triggerMills(startMills, endMills, percent)
            assertEquals(triggerMills, mills)
        }
    }

    @RunWith(JUnit4::class)
    class CalculateTriggerMillsTest1 {

        @Test
        fun checkNegativeStartDate() {
            try {
                AlarmRepoImpl.triggerMills(-1, 100, 1F)
                Assert.fail("Test should throw IllegalArgumentException at this moment.")
            } catch (e: IllegalArgumentException) {
                assertNotNull(e.message)
            }
        }

        @Test
        fun checkNegativeEndDate() {
            try {
                AlarmRepoImpl.triggerMills(0, -1, 1F)
                Assert.fail("Test should throw IllegalArgumentException at this moment.")
            } catch (e: IllegalArgumentException) {
                assertNotNull(e.message)
            }
        }

        @Test
        fun checkStartDateGreaterThanEndDate() {
            try {
                AlarmRepoImpl.triggerMills(100, 99, 1F)
                Assert.fail("Test should throw IllegalArgumentException at this moment.")
            } catch (e: IllegalArgumentException) {
                assertNotNull(e.message)
            }
        }
    }
}
