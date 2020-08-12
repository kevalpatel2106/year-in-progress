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
}
