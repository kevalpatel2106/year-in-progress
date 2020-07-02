package com.kevalpatel2106.yip.repo.dto

import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
class DeadlineDtoToEntityTest {

    private val nowMills = System.currentTimeMillis()
    private val baseDeadlineDto = DeadlineDto(
        id = 23L,
        title = "XYZ",
        type = DeadlineType.DAY_DEADLINE,
        color = DeadlineColor.COLOR_BLUE,
        notifications = arrayListOf(0.1F),
        start = Date(nowMills),
        end = Date(nowMills + 60_000)
    )


    @Test
    fun checkDtoTiEntity_withEndDateGreaterThanStartDate() {
        val dto = baseDeadlineDto.copy()
        val entity = dto.toEntity(Date(nowMills))

        assertEquals(dto.id, entity.id)
        assertEquals(dto.title, entity.title)
        assertEquals(dto.color, entity.color)
        assertEquals(dto.start, entity.start)
        assertEquals(dto.end, entity.end)
        assertEquals(dto.notifications.size, entity.notificationPercent.size)
        assertEquals(dto.notifications.first(), entity.notificationPercent.first())
        assertEquals(dto.type, entity.deadlineType)

        val percent = calculatePercent(Date(nowMills), dto.start, dto.end)
        assertEquals(percent, entity.percent)
    }

    @Test
    fun checkDtoTiEntity_withEndDateLessThanStartDate() {
        val dto = baseDeadlineDto.copy(end = Date(nowMills - 60_000))
        val entity = dto.toEntity(Date(nowMills))

        assertEquals(dto.id, entity.id)
        assertEquals(dto.title, entity.title)
        assertEquals(dto.color, entity.color)
        assertEquals(dto.start, entity.start)
        assertEquals(dto.end, entity.end)
        assertEquals(dto.notifications.size, entity.notificationPercent.size)
        assertEquals(dto.notifications.first(), entity.notificationPercent.first())
        assertEquals(dto.type, entity.deadlineType)

        val percent = calculatePercent(Date(nowMills), dto.start, dto.end)
        assertEquals(percent, entity.percent)
    }

    @Test
    fun checkDtoTiEntity_withEndDateSameAsStartDate() {
        val dto = baseDeadlineDto.copy(end = Date(nowMills))
        val entity = dto.toEntity(Date(nowMills))

        assertEquals(dto.id, entity.id)
        assertEquals(dto.title, entity.title)
        assertEquals(dto.color, entity.color)
        assertEquals(dto.start, entity.start)
        assertEquals(dto.end, entity.end)
        assertEquals(dto.notifications.size, entity.notificationPercent.size)
        assertEquals(dto.notifications.first(), entity.notificationPercent.first())
        assertEquals(dto.type, entity.deadlineType)

        val percent = calculatePercent(Date(nowMills), dto.start, dto.end)
        assertEquals(percent, entity.percent)
    }
}
