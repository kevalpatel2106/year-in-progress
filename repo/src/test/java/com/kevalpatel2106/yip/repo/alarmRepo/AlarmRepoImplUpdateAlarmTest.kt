package com.kevalpatel2106.yip.repo.alarmRepo

import android.app.AlarmManager
import android.os.Build
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AlarmRepoImplUpdateAlarmTest : AlarmRepoImplTestSetUp() {
    private val deadlineListSubject = BehaviorSubject.create<List<DeadlineDto>>()

    @Before
    fun beforeTest() {
        val deadlineListFlowable = deadlineListSubject.toFlowable(BackpressureStrategy.DROP)
        whenever(deadlineDao.observeAll()).thenReturn(deadlineListFlowable)
    }

    @Test
    fun `given no deadline when get update alarms called check set alarm never called`() {
        // given
        deadlineListSubject.onNext(listOf())

        // when
        alarmRepo.updateAlarms(TestBroadCastReceiver::class.java)

        // then
        verify(alarmManager, never()).run {
            if (Build.VERSION.SDK_INT >= 23) {
                setExactAndAllowWhileIdle(anyInt(), anyLong(), any())
            } else {
                setExact(anyInt(), anyLong(), any())
            }
        }
    }

    @Test
    fun `given deadline with trigger time before current time when get update alarms called check set alarm never called`() {
        // given
        val triggerPercent = kFixture.range(0f..49.99f)
        val deadline = generateCustomDeadlineDto(listOf(triggerPercent))
        deadlineListSubject.onNext(listOf(deadline))

        // when
        alarmRepo.updateAlarms(TestBroadCastReceiver::class.java)

        // then
        verify(alarmManager, never()).run {
            if (Build.VERSION.SDK_INT >= 23) {
                setExactAndAllowWhileIdle(anyInt(), anyLong(), any())
            } else {
                setExact(anyInt(), anyLong(), any())
            }
        }
    }

    @Test
    fun `given deadline with trigger time after current time when get update alarms called check alarm type`() {
        // given
        val triggerPercent = kFixture.range(51f..100f)
        val deadline = generateCustomDeadlineDto(listOf(triggerPercent))
        deadlineListSubject.onNext(listOf(deadline))

        // when
        alarmRepo.updateAlarms(TestBroadCastReceiver::class.java)

        // then
        val alarmTypeCaptor = argumentCaptor<Int>()
        verify(alarmManager).run {
            if (Build.VERSION.SDK_INT >= 23) {
                setExactAndAllowWhileIdle(alarmTypeCaptor.capture(), anyLong(), any())
            } else {
                setExact(alarmTypeCaptor.capture(), anyLong(), any())
            }
        }
        assertEquals(AlarmManager.RTC_WAKEUP, alarmTypeCaptor.lastValue)
    }

    @Test
    fun `given deadline with trigger time after current time when get update alarms called check trigger mills`() {
        // given
        val triggerPercent = kFixture.range(51f..100f)
        val deadline = generateCustomDeadlineDto(listOf(triggerPercent))
        deadlineListSubject.onNext(listOf(deadline))

        // when
        alarmRepo.updateAlarms(TestBroadCastReceiver::class.java)

        // then
        val triggerMillsCaptor = argumentCaptor<Long>()
        verify(alarmManager).run {
            if (Build.VERSION.SDK_INT >= 23) {
                setExactAndAllowWhileIdle(anyInt(), triggerMillsCaptor.capture(), any())
            } else {
                setExact(anyInt(), triggerMillsCaptor.capture(), any())
            }
        }
        assertEquals(
            alarmRepo.triggerMills(deadline.start.time, deadline.end.time, triggerPercent),
            triggerMillsCaptor.lastValue
        )
    }
}
