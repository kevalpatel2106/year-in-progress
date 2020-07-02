package com.kevalpatel2106.yip.repo.dto

import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.utils.endOfTheDay
import com.kevalpatel2106.yip.repo.utils.endOfTheMonth
import com.kevalpatel2106.yip.repo.utils.endOfTheWeek
import com.kevalpatel2106.yip.repo.utils.endOfTheYear
import com.kevalpatel2106.yip.repo.utils.getFirstDayOfQuarter
import com.kevalpatel2106.yip.repo.utils.getLastDayOfQuarter
import com.kevalpatel2106.yip.repo.utils.startOfTheDay
import com.kevalpatel2106.yip.repo.utils.startOfTheMonth
import com.kevalpatel2106.yip.repo.utils.startOfTheWeek
import com.kevalpatel2106.yip.repo.utils.startOfTheYear
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Calendar
import java.util.Date

@RunWith(Parameterized::class)
internal class ModifyPrebuiltDeadlineTest(
    private val input: DeadlineDto,
    private val output: DeadlineDto
) {

    companion object {
        private val nowMills = System.currentTimeMillis()
        private val nowCal: Calendar
            get() = Calendar.getInstance().apply { timeInMillis = nowMills }
        private val baseDeadline = DeadlineDto(
            id = 23L,
            title = "XYZ",
            type = DeadlineType.DAY_DEADLINE,
            color = DeadlineColor.COLOR_BLUE,
            notifications = arrayListOf(0.1F),
            start = Date(nowMills),
            end = Date(nowMills + 6000)
        )

        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<DeadlineDto>> {

            return arrayListOf(
                arrayOf(
                    baseDeadline.copy(type = DeadlineType.DAY_DEADLINE),
                    baseDeadline.copy(
                        type = DeadlineType.DAY_DEADLINE,
                        start = nowCal.startOfTheDay(),
                        end = nowCal.endOfTheDay()
                    )
                ),
                arrayOf(
                    baseDeadline.copy(type = DeadlineType.WEEK_DEADLINE),
                    baseDeadline.copy(
                        type = DeadlineType.WEEK_DEADLINE,
                        start = nowCal.startOfTheWeek(),
                        end = nowCal.endOfTheWeek()
                    )
                ),
                arrayOf(
                    baseDeadline.copy(type = DeadlineType.MONTH_DEADLINE),
                    baseDeadline.copy(
                        type = DeadlineType.MONTH_DEADLINE,
                        start = nowCal.startOfTheMonth(),
                        end = nowCal.endOfTheMonth()
                    )
                ),
                arrayOf(
                    baseDeadline.copy(type = DeadlineType.QUARTER_DEADLINE),
                    baseDeadline.copy(
                        type = DeadlineType.QUARTER_DEADLINE,
                        start = nowCal.getFirstDayOfQuarter(),
                        end = nowCal.getLastDayOfQuarter()
                    )
                ),
                arrayOf(
                    baseDeadline.copy(type = DeadlineType.YEAR_DEADLINE),
                    baseDeadline.copy(
                        type = DeadlineType.YEAR_DEADLINE,
                        start = nowCal.startOfTheYear(),
                        end = nowCal.endOfTheYear()
                    )
                ),
                arrayOf(
                    baseDeadline.copy(type = DeadlineType.CUSTOM),
                    baseDeadline.copy(type = DeadlineType.CUSTOM)
                )
            )
        }
    }

    @Test
    fun checkModifyPrebuiltDeadline() {
        val modified = input.modifyPrebuiltDeadline(Date(nowMills))
        assertTrue(modified === input)

        assertEquals(output.id, modified.id)
        assertEquals(output.title, modified.title)
        assertEquals(output.color, modified.color)
        assertEquals(output.type, modified.type)
        assertEquals(output.notifications.size, modified.notifications.size)
        assertEquals(output.notifications.first(), modified.notifications.first())
        assertEquals(output.start.time, modified.start.time)
        assertEquals(output.end.time, modified.end.time)
    }
}
