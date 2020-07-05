package com.kevalpatel2106.yip.repo.deadlineRepo

import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import java.util.Date

@RunWith(JUnit4::class)
internal class DeadlineRepoImplObserveDeadlineTest : DeadlineRepoImplTestSetUp() {

    @Test
    fun `given deadline id when observing deadline success check result`() {
        // given
        val deadlineDto = generateCustomDeadlineDto()
        deadlineSubject.onNext(deadlineDto)
        minuteObserverSubject.onNext(Date())

        // when
        val testSubscriber = deadlineRepo.observeDeadline(deadlineDto.id).test()
        testSubscriber.awaitCount(1)

        // then
        assertDeadlineInfo(testSubscriber, deadlineDto, 1)
    }

    @Test
    fun `given deadline id when observing deadline error occurs check error message`() {
        // given
        val throwable = Throwable(kFixture<String>())
        whenever(deadlineDao.observe(ArgumentMatchers.anyLong()))
            .thenReturn(Flowable.error(throwable))
        minuteObserverSubject.onNext(Date())

        // when
        val testSubscriber = deadlineRepo.observeDeadline(kFixture()).test()
        testSubscriber.await()

        // then
        testSubscriber.assertNoTimeout()
            .assertError { it.message == throwable.message }
            .assertTerminated()

    }

    @Test
    fun `given observing deadline when deadline changes check result updated`() {
        // given
        val oldDeadlineDto = generateCustomDeadlineDto()
        val newDeadlineDto = generateCustomDeadlineDto()
        deadlineSubject.onNext(oldDeadlineDto)
        minuteObserverSubject.onNext(Date())
        val testSubscriber = deadlineRepo.observeDeadline(oldDeadlineDto.id).test()
        testSubscriber.awaitCount(1)

        // when
        deadlineSubject.onNext(newDeadlineDto)
        testSubscriber.awaitCount(2)

        // then
        assertDeadlineInfo(testSubscriber, newDeadlineDto, 2)
    }

    private fun assertDeadlineInfo(
        testSubscriber: TestSubscriber<Deadline>,
        deadlineDto: DeadlineDto,
        expectedCount: Int
    ) {
        testSubscriber.assertNoTimeout()
            .assertNoErrors()
            .assertNotTerminated()
            .assertValueCount(expectedCount)
            .assertValueAt(expectedCount - 1) { it.id == deadlineDto.id }
            .assertValueAt(expectedCount - 1) { it.title == deadlineDto.title }
            .assertValueAt(expectedCount - 1) { it.color == deadlineDto.color }
            .assertValueAt(expectedCount - 1) { it.deadlineType == deadlineDto.type }
            .assertValueAt(expectedCount - 1) { it.notificationPercent.size == deadlineDto.notifications.size }
    }
}
