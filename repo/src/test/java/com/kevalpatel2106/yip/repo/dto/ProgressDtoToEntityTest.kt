package com.kevalpatel2106.yip.repo.dto

import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
class ProgressDtoToEntityTest {

    private val nowMills = System.currentTimeMillis()
    private val baseProgressDto = ProgressDto(
        id = 23L,
        title = "XYZ",
        progressType = ProgressType.DAY_PROGRESS,
        color = ProgressColor.COLOR_BLUE,
        notifications = arrayListOf(0.1F),
        start = Date(nowMills),
        end = Date(nowMills + 60_000)
    )


    @Test
    fun checkDtoTiEntity_withEndDateGreaterThanStartDate() {
        val dto = baseProgressDto.copy()
        val entity = dto.toEntity(Date(nowMills))

        assertEquals(dto.id, entity.id)
        assertEquals(dto.title, entity.title)
        assertEquals(dto.color, entity.color)
        assertEquals(dto.start, entity.start)
        assertEquals(dto.end, entity.end)
        assertEquals(dto.notifications.size, entity.notificationPercent.size)
        assertEquals(dto.notifications.first(), entity.notificationPercent.first())
        assertEquals(dto.progressType, entity.progressType)

        val percent = calculatePercent(Date(nowMills), dto.start, dto.end)
        assertEquals(percent, entity.percent)
    }

    @Test
    fun checkDtoTiEntity_withEndDateLessThanStartDate() {
        val dto = baseProgressDto.copy(end = Date(nowMills - 60_000))
        val entity = dto.toEntity(Date(nowMills))

        assertEquals(dto.id, entity.id)
        assertEquals(dto.title, entity.title)
        assertEquals(dto.color, entity.color)
        assertEquals(dto.start, entity.start)
        assertEquals(dto.end, entity.end)
        assertEquals(dto.notifications.size, entity.notificationPercent.size)
        assertEquals(dto.notifications.first(), entity.notificationPercent.first())
        assertEquals(dto.progressType, entity.progressType)

        val percent = calculatePercent(Date(nowMills), dto.start, dto.end)
        assertEquals(percent, entity.percent)
    }

    @Test
    fun checkDtoTiEntity_withEndDateSameAsStartDate() {
        val dto = baseProgressDto.copy(end = Date(nowMills))
        val entity = dto.toEntity(Date(nowMills))

        assertEquals(dto.id, entity.id)
        assertEquals(dto.title, entity.title)
        assertEquals(dto.color, entity.color)
        assertEquals(dto.start, entity.start)
        assertEquals(dto.end, entity.end)
        assertEquals(dto.notifications.size, entity.notificationPercent.size)
        assertEquals(dto.notifications.first(), entity.notificationPercent.first())
        assertEquals(dto.progressType, entity.progressType)

        val percent = calculatePercent(Date(nowMills), dto.start, dto.end)
        assertEquals(percent, entity.percent)
    }
}
