package com.kevalpatel2106.yip.repo.alarmRepo

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.Parameterized

@RunWith(Enclosed::class)
internal class AlarmRepoImplCalculateTriggerMillsTest {

    @RunWith(Parameterized::class)
    class ValidInputTest(
        private val startMills: Long,
        private val endMills: Long,
        private val triggerPercent: Float,
        private val triggerMills: Long
    ) : AlarmRepoImplTestSetUp() {

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
        fun `given start-end time and trigger percent check alarm trigger time`() {
            val mills = alarmRepo.triggerMills(startMills, endMills, triggerPercent)
            assertEquals(triggerMills, mills)
        }
    }

    @RunWith(JUnit4::class)
    class InvalidInputsTest : AlarmRepoImplTestSetUp() {

        @Test
        fun `given negative start date when calculating trigger mills check it fails`() {
            try {
                alarmRepo.triggerMills(-1, 100, 1F)
                Assert.fail("Test should throw IllegalArgumentException at this moment.")
            } catch (e: IllegalArgumentException) {
                Assert.assertNotNull(e.message)
            }
        }

        @Test
        fun `given negative end date when calculating trigger mills check it fails`() {
            try {
                alarmRepo.triggerMills(0, -1, 1F)
                Assert.fail("Test should throw IllegalArgumentException at this moment.")
            } catch (e: IllegalArgumentException) {
                Assert.assertNotNull(e.message)
            }
        }

        @Test
        fun `given start date after end date when calculating trigger mills check it fails`() {
            try {
                alarmRepo.triggerMills(100, 99, 1F)
                Assert.fail("Test should throw IllegalArgumentException at this moment.")
            } catch (e: IllegalArgumentException) {
                Assert.assertNotNull(e.message)
            }
        }
    }
}
