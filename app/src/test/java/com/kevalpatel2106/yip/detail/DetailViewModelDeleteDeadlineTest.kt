package com.kevalpatel2106.yip.detail

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.generateDeadline
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DetailViewModelDeleteDeadlineTest : DetailViewModelTestSetUp() {

    @Test
    fun `given deadline delete success when delete confirmed check user message displayed`() {
        // given
        val deadline = generateDeadline(kFixture)
        model.setDeadlineIdToMonitor(deadline.id)
        whenever(deadlineRepo.deleteDeadline(deadline.id)).thenReturn(Completable.complete())

        // when
        model.onDeleteDeadlineConfirmed()

        // check
        val singleEvent = model.singleEvent.getOrAwaitValue()
        assertEquals(deadlineDeletedMessage, (singleEvent as ShowUserMessage).message)
    }

    @Test
    fun `given deadline delete success when delete confirmed check screen closes`() {
        // given
        val deadline = generateDeadline(kFixture)
        model.setDeadlineIdToMonitor(deadline.id)
        whenever(deadlineRepo.deleteDeadline(deadline.id)).thenReturn(Completable.complete())

        // when
        model.onDeleteDeadlineConfirmed()

        // check
        val singleEvent = model.singleEvent.getOrAwaitValue()
        assertTrue((singleEvent as ShowUserMessage).closeScreen)
    }

    @Test
    fun `given deadline delete fail when delete confirmed check error message displayed`() {
        // given
        val deadline = generateDeadline(kFixture)
        model.setDeadlineIdToMonitor(deadline.id)
        whenever(deadlineRepo.deleteDeadline(deadline.id)).thenReturn(Completable.error(Throwable()))

        // when
        model.onDeleteDeadlineConfirmed()

        // check
        val singleEvent = model.singleEvent.getOrAwaitValue()
        assertEquals(deadlineDeleteErrorMessage, (singleEvent as ShowUserMessage).message)
    }

    @Test
    fun `given deadline delete fail when delete confirmed check screen stays open`() {
        // given
        val deadline = generateDeadline(kFixture)
        model.setDeadlineIdToMonitor(deadline.id)
        whenever(deadlineRepo.deleteDeadline(deadline.id)).thenReturn(Completable.error(Throwable()))

        // when
        model.onDeleteDeadlineConfirmed()

        // check
        val singleEvent = model.singleEvent.getOrAwaitValue()
        assertFalse((singleEvent as ShowUserMessage).closeScreen)
    }

    @Test
    fun `given deadline deleting when delete confirmed check view state`() {
        // given
        val deadline = generateDeadline(kFixture)
        model.setDeadlineIdToMonitor(deadline.id)
        whenever(deadlineRepo.deleteDeadline(deadline.id))
            .thenReturn(PublishSubject.create<Unit>().ignoreElements())

        // when
        model.onDeleteDeadlineConfirmed()

        // check
        val viewState = model.viewState.getOrAwaitValue()
        assertTrue(viewState.isDeleting)
    }

    @Test
    fun `given deadline delete success when delete confirmed check view state`() {
        // given
        val deadline = generateDeadline(kFixture)
        model.setDeadlineIdToMonitor(deadline.id)
        whenever(deadlineRepo.deleteDeadline(deadline.id)).thenReturn(Completable.complete())

        // when
        model.onDeleteDeadlineConfirmed()

        // check
        val viewState = model.viewState.getOrAwaitValue()
        assertFalse(viewState.isDeleting)
    }

    @Test
    fun `given deadline delete error when delete confirmed check view state`() {
        // given
        val deadline = generateDeadline(kFixture)
        model.setDeadlineIdToMonitor(deadline.id)
        whenever(deadlineRepo.deleteDeadline(deadline.id)).thenReturn(Completable.error(Throwable()))

        // when
        model.onDeleteDeadlineConfirmed()

        // check
        val viewState = model.viewState.getOrAwaitValue()
        assertFalse(viewState.isDeleting)
    }
}
