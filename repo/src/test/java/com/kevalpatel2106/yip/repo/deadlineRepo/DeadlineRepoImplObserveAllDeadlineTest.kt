package com.kevalpatel2106.yip.repo.deadlineRepo

import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
import io.reactivex.subscribers.TestSubscriber
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
internal class DeadlineRepoImplObserveAllDeadlineTest : DeadlineRepoImplTestSetUp() {

    @Before
    fun before() {
        minuteObserverSubject.onNext(Date(System.currentTimeMillis()))
    }

    @Test
    fun `given list of deadline and sort order a to z when monitor deadline called check list`() {
        // given
        val deadlines = getDeadlineList()
        deadlineListSubject.onNext(deadlines)
        sortOrderPrefSubject.onNext(sortOrderAtoZ)

        // when
        val testSubscriber = deadlineRepo.observeAllDeadlines().test()
        testSubscriber.awaitCount(1)

        // check
        testSubscriber.assertValueCount(1)
        val sortedList = deadlines.sortedBy { it.title }.map { it.id }
        assertDeadlineList(testSubscriber, sortedList)
    }

    @Test
    fun `given list of deadline and sort order z to a when monitor deadline called check list`() {
        // given
        val deadlines = getDeadlineList()
        deadlineListSubject.onNext(deadlines)
        sortOrderPrefSubject.onNext(sortOrderZtoA)

        // when
        val testSubscriber = deadlineRepo.observeAllDeadlines().test()
        testSubscriber.awaitCount(1)

        // check
        val sortedList = deadlines.sortedByDescending { it.title }.map { it.id }
        testSubscriber.assertValueCount(1)
        assertDeadlineList(testSubscriber, sortedList)
    }

    @Test
    fun `given list of deadline and sort order finish time desc when monitor deadline called check list`() {
        // given
        val deadlines = getDeadlineList()
        deadlineListSubject.onNext(deadlines)
        sortOrderPrefSubject.onNext(sortOrderEndingTimeDesc)

        // when
        val testSubscriber = deadlineRepo.observeAllDeadlines().test()
        testSubscriber.awaitCount(1)

        // check
        testSubscriber.assertValueCount(1)
        val sortedList = deadlines.sortedByDescending { it.end.time }.map { it.id }
        assertDeadlineList(testSubscriber, sortedList)
    }

    @Test
    fun `given list of deadline and sort order finish time asc when monitor deadline called check list`() {
        // given
        val deadlines = getDeadlineList()
        deadlineListSubject.onNext(deadlines)
        sortOrderPrefSubject.onNext(sortOrderEndingTimeAsc)

        // when
        val testSubscriber = deadlineRepo.observeAllDeadlines().test()
        testSubscriber.awaitCount(1)

        // check
        testSubscriber.assertValueCount(1)
        val sortedList = deadlines.sortedBy { it.end.time }.map { it.id }
        assertDeadlineList(testSubscriber, sortedList)
    }

    @Test
    fun `given list of deadline empty when monitor deadline called check output list is empty`() {
        // given
        val deadlines = listOf<DeadlineDto>()
        deadlineListSubject.onNext(deadlines)
        sortOrderPrefSubject.onNext(randomSortOrder())

        // when
        val testSubscriber = deadlineRepo.observeAllDeadlines().test()
        testSubscriber.awaitCount(1)

        // check
        testSubscriber.assertNoErrors()
            .assertNotTerminated()
            .assertValueCount(1)
            .assertValueAt(0) { it.isEmpty() }
    }

    @Test
    fun `given list of deadline and sort order invalid when monitor deadline called check error`() {
        // given
        val deadlines = getDeadlineList()
        deadlineListSubject.onNext(deadlines)
        sortOrderPrefSubject.onNext(kFixture())

        // when
        val testSubscriber = deadlineRepo.observeAllDeadlines().test()
        testSubscriber.awaitCount(1)

        // check
        testSubscriber.assertError { it is IllegalArgumentException }
    }

    @Test
    fun `given monitoring list of deadline when sort order changes to time desc check list`() {
        // given
        val deadlines = getDeadlineList()
        deadlineListSubject.onNext(deadlines)
        sortOrderPrefSubject.onNext(randomSortOrder())
        val testSubscriber = deadlineRepo.observeAllDeadlines().test()
        testSubscriber.awaitCount(1)

        // when
        sortOrderPrefSubject.onNext(sortOrderEndingTimeDesc)
        testSubscriber.awaitCount(2)

        // check
        testSubscriber.assertValueCount(2)
        val sortedList = deadlines.sortedByDescending { it.end.time }.map { it.id }
        assertDeadlineList(testSubscriber, sortedList)
    }

    @Test
    fun `given monitoring list of deadline when deadline list changes check resulting list updated`() {
        // given
        val oldDeadlines = getDeadlineList()
        deadlineListSubject.onNext(oldDeadlines)
        sortOrderPrefSubject.onNext(sortOrderEndingTimeDesc)
        val testSubscriber = deadlineRepo.observeAllDeadlines().test()
        testSubscriber.awaitCount(1)

        // when
        val newDeadlines = getDeadlineList()
        deadlineListSubject.onNext(newDeadlines)
        testSubscriber.awaitCount(2)

        // check
        testSubscriber.assertValueCount(2)
        val sortedList = newDeadlines.sortedByDescending { it.end.time }.map { it.id }
        assertDeadlineList(testSubscriber, sortedList)
    }


    @Test
    fun `given monitoring list of deadline when minute observer changes check resulting list updates`() {
        // given
        val deadlines = getDeadlineList()
        deadlineListSubject.onNext(deadlines)
        sortOrderPrefSubject.onNext(randomSortOrder())
        val testSubscriber = deadlineRepo.observeAllDeadlines().test()
        testSubscriber.awaitCount(1)

        // when
        minuteObserverSubject.onNext(Date(System.currentTimeMillis()))
        testSubscriber.awaitCount(2)

        // check
        testSubscriber.assertNoErrors()
            .assertNotTerminated()
            .assertNoTimeout()
            .assertValueCount(2)
            .assertValueAt(1) { it.isNotEmpty() }
    }


    private fun assertDeadlineList(
        testSubscriber: TestSubscriber<List<Deadline>>,
        sortedListIds: List<Long>
    ) {
        testSubscriber.assertNoErrors().assertNotTerminated().assertNoTimeout()
        testSubscriber.values().last().forEachIndexed { index, deadline ->
            assertEquals(deadline.id, sortedListIds[index])
        }
    }
}
