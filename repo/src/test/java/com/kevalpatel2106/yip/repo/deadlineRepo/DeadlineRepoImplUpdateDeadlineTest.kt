package com.kevalpatel2106.yip.repo.deadlineRepo

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
internal class DeadlineRepoImplUpdateDeadlineTest : DeadlineRepoImplTestSetUp() {

    @Before
    fun before() {
        whenever(timeProvider.nowAsync()).thenReturn(Single.just(Date()))
    }

    @Test
    fun `given deadline info when deadline added successfully check result deadline`() {
        // given
        val insertedDeadlineId = kFixture<Long>()
        val deadlineDto = generateCustomDeadlineDto()
        whenever(deadlineDao.insert(any())).thenReturn(insertedDeadlineId)

        // when
        val testSubscriber = deadlineRepo.addUpdateDeadline(
            deadlineDto.id,
            deadlineDto.title,
            deadlineDto.color,
            deadlineDto.start,
            deadlineDto.end,
            deadlineDto.type,
            deadlineDto.notifications
        ).test()
        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS)

        // then
        testSubscriber.assertNoTimeout()
            .assertTerminated()
            .assertNoErrors()
            .assertValueAt(0) { it.id == insertedDeadlineId }
            .assertValueAt(0) { it.title == deadlineDto.title }
            .assertValueAt(0) { it.color == deadlineDto.color }
            .assertValueAt(0) { it.start == deadlineDto.start }
            .assertValueAt(0) { it.end == deadlineDto.end }
            .assertValueAt(0) { it.deadlineType == deadlineDto.type }
            .assertValueAt(0) { it.notificationPercent == deadlineDto.notifications }
    }

    @Test
    fun `given deadline info when deadline added successfully check current time gets called`() {
        // given
        val insertedDeadlineId = kFixture<Long>()
        val deadlineDto = generateCustomDeadlineDto()
        whenever(deadlineDao.insert(any())).thenReturn(insertedDeadlineId)

        // when
        val testSubscriber = deadlineRepo.addUpdateDeadline(
            deadlineDto.id,
            deadlineDto.title,
            deadlineDto.color,
            deadlineDto.start,
            deadlineDto.end,
            deadlineDto.type,
            deadlineDto.notifications
        ).test()
        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS)

        // then
        verify(timeProvider).nowAsync()
    }

    @Test
    fun `given deadline info when error occures inserting deadline check error message`() {
        // given
        val exception = IOException(kFixture<String>())
        doThrow(exception).whenever(deadlineDao).insert(any())

        // when
        val deadlineDto = generateCustomDeadlineDto()
        val testSubscriber = deadlineRepo.addUpdateDeadline(
            deadlineDto.id,
            deadlineDto.title,
            deadlineDto.color,
            deadlineDto.start,
            deadlineDto.end,
            deadlineDto.type,
            deadlineDto.notifications
        ).test()
        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS)

        // then
        testSubscriber.assertNoTimeout()
            .assertTerminated()
            .assertError { it.message == exception.message }
    }
}
