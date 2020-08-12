package com.kevalpatel2106.yip.entity.ext

import com.kevalpatel2106.testutils.getKFixture
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(Enclosed::class)
class DeadlineExtKtTest {

    @RunWith(Parameterized::class)
    class IsFinishedTest(
        private val now: Long,
        private val deadline: Deadline,
        private val isFinished: Boolean
    ) {
        companion object {
            private val kFixture = getKFixture()
            private val baseDeadline = Deadline(
                id = kFixture(),
                title = kFixture(),
                description = kFixture(),
                color = DeadlineColor.COLOR_BLUE,
                end = kFixture(),
                start = kFixture(),
                deadlineType = DeadlineType.DAY_DEADLINE,
                notificationPercent = kFixture(),
                percent = kFixture()
            )
            private val nowMills = System.currentTimeMillis()

            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any?>> {
                return arrayListOf(
                    arrayOf(
                        nowMills,
                        baseDeadline.copy(end = Date().apply { time = nowMills }),
                        true
                    ),
                    arrayOf(
                        nowMills,
                        baseDeadline.copy(
                            end = Date().apply { time = nowMills - kFixture.range(1..1000) }
                        ),
                        true
                    ),
                    arrayOf(
                        nowMills,
                        baseDeadline.copy(
                            end = Date().apply { time = nowMills + kFixture.range(1..1000) }
                        ),
                        false
                    )
                )
            }
        }

        @Test
        fun checkIsDeadlineFinished() {
            assertEquals(isFinished, deadline.isFinished(now))
        }
    }

    @RunWith(Parameterized::class)
    class TimeLeftDHMTest(
        private val now: Long,
        private val deadline: Deadline,
        private val dhm: Triple<Long, Long, Long>
    ) {
        companion object {
            private val kFixture = getKFixture()
            private val baseDeadline = Deadline(
                id = kFixture(),
                title = kFixture(),
                description = kFixture(),
                color = DeadlineColor.COLOR_BLUE,
                end = kFixture(),
                start = kFixture(),
                deadlineType = DeadlineType.DAY_DEADLINE,
                notificationPercent = kFixture(),
                percent = kFixture()
            )
            private val nowMills = System.currentTimeMillis()

            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any?>> {
                return arrayListOf(
                    arrayOf(
                        nowMills,
                        baseDeadline.copy(end = Date().apply { time = nowMills }),
                        Triple(0, 0, 0)
                    ),
                    arrayOf(
                        nowMills,
                        baseDeadline.copy(
                            end = Date().apply { time = nowMills - kFixture.range(1..1000) }
                        ),
                        Triple(0, 0, 0)
                    ),
                    arrayOf(
                        nowMills,
                        baseDeadline.copy(
                            end = Date().apply { time = nowMills + TimeUnit.DAYS.toMillis(1) }
                        ),
                        Triple(1, 0, 0)
                    ),
                    arrayOf(
                        nowMills,
                        baseDeadline.copy(
                            end = Date().apply { time = nowMills + TimeUnit.HOURS.toMillis(25) }
                        ),
                        Triple(1, 1, 0)
                    ),
                    arrayOf(
                        nowMills,
                        baseDeadline.copy(
                            end = Date().apply { time = nowMills + TimeUnit.MINUTES.toMillis(65) }
                        ),
                        Triple(0, 1, 5)
                    ),
                    arrayOf(
                        nowMills,
                        baseDeadline.copy(
                            end = Date().apply { time = nowMills + TimeUnit.MINUTES.toMillis(1) }
                        ),
                        Triple(0, 0, 1)
                    ),
                    arrayOf(
                        nowMills,
                        baseDeadline.copy(
                            end = Date().apply { time = nowMills + TimeUnit.SECONDS.toMillis(65) }
                        ),
                        Triple(0, 0, 1)
                    )
                )
            }
        }

        @Test
        fun checkIsDeadlineFinished() {
            val (day, hour, min) = deadline.timeLeftDHM(now)
            assertEquals(dhm.first, day)
            assertEquals(dhm.second, hour)
            assertEquals(dhm.third, min)
        }
    }
}
