package com.kevalpatel2106.yip.repo.dto

import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
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
internal class ModifyPrebuiltProgressTest(
    private val input: ProgressDto,
    private val output: ProgressDto
) {

    companion object {
        private val nowMills = System.currentTimeMillis()
        private val nowCal: Calendar
            get() = Calendar.getInstance().apply { timeInMillis = nowMills }
        private val baseProgress = ProgressDto(
            id = 23L,
            title = "XYZ",
            progressType = ProgressType.DAY_PROGRESS,
            color = ProgressColor.COLOR_BLUE,
            notifications = arrayListOf(0.1F),
            start = Date(nowMills),
            end = Date(nowMills + 6000)
        )

        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<ProgressDto>> {

            return arrayListOf(
                arrayOf(
                    baseProgress.copy(progressType = ProgressType.DAY_PROGRESS),
                    baseProgress.copy(
                        progressType = ProgressType.DAY_PROGRESS,
                        start = nowCal.startOfTheDay(),
                        end = nowCal.endOfTheDay()
                    )
                ),
                arrayOf(
                    baseProgress.copy(progressType = ProgressType.WEEK_PROGRESS),
                    baseProgress.copy(
                        progressType = ProgressType.WEEK_PROGRESS,
                        start = nowCal.startOfTheWeek(),
                        end = nowCal.endOfTheWeek()
                    )
                ),
                arrayOf(
                    baseProgress.copy(progressType = ProgressType.MONTH_PROGRESS),
                    baseProgress.copy(
                        progressType = ProgressType.MONTH_PROGRESS,
                        start = nowCal.startOfTheMonth(),
                        end = nowCal.endOfTheMonth()
                    )
                ),
                arrayOf(
                    baseProgress.copy(progressType = ProgressType.QUARTER_PROGRESS),
                    baseProgress.copy(
                        progressType = ProgressType.QUARTER_PROGRESS,
                        start = nowCal.getFirstDayOfQuarter(),
                        end = nowCal.getLastDayOfQuarter()
                    )
                ),
                arrayOf(
                    baseProgress.copy(progressType = ProgressType.YEAR_PROGRESS),
                    baseProgress.copy(
                        progressType = ProgressType.YEAR_PROGRESS,
                        start = nowCal.startOfTheYear(),
                        end = nowCal.endOfTheYear()
                    )
                ),
                arrayOf(
                    baseProgress.copy(progressType = ProgressType.CUSTOM),
                    baseProgress.copy(progressType = ProgressType.CUSTOM)
                )
            )
        }
    }

    @Test
    fun checkModifyPrebuiltProgress() {
        val modified = input.modifyPrebuiltProgress(Date(nowMills))
        assertTrue(modified === input)

        assertEquals(output.id, modified.id)
        assertEquals(output.title, modified.title)
        assertEquals(output.color, modified.color)
        assertEquals(output.progressType, modified.progressType)
        assertEquals(output.notifications.size, modified.notifications.size)
        assertEquals(output.notifications.first(), modified.notifications.first())
        assertEquals(output.start.time, modified.start.time)
        assertEquals(output.end.time, modified.end.time)
    }
}
