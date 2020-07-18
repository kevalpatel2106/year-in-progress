package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.entity.DeadlineType
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class EditViewDeadlineLoadDetailsTest : EditViewDeadlineModelTestSetUp() {

    @Test
    fun `given new deadline id when set deadline id called check deadline observing started`() {
        // given
        val deadlineId = kFixture<Long>()

        // when
        viewModel.setDeadlineId(deadlineId)

        // then
        verify(deadlineRepo).observeDeadline(deadlineId)
    }

    @Test
    fun `given deadline id set when same deadline set check observing deadline only once`() {
        // given
        val deadlineId = kFixture<Long>()
        viewModel.setDeadlineId(deadlineId)

        // when
        viewModel.setDeadlineId(deadlineId)

        // then
        verify(deadlineRepo).observeDeadline(deadlineId)
    }

    @Test
    fun `given new deadline id when same deadline set check observing deadline not called`() {
        // given
        val deadlineId = 0L

        // when
        viewModel.setDeadlineId(deadlineId)

        // then
        verify(deadlineRepo, never()).observeDeadline(deadlineId)
    }

    @Test
    fun `when deadline loading in progress check is loading changes to true in view state`() {
        // given
        val deadline = generateDeadline()

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertTrue(viewModel.viewState.getOrAwaitValue().isLoading)
    }

    @Test
    fun `when deadline loaded check is loading changes to false in view state`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertFalse(viewModel.viewState.getOrAwaitValue().isLoading)
    }

    @Test
    fun `given deadline when set deadline id check deadline type in view state`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertEquals(deadline.deadlineType, viewModel.viewState.getOrAwaitValue().type)
    }

    @Test
    fun `given deadline when set deadline id check initial title in view state`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertEquals(deadline.title, viewModel.viewState.getOrAwaitValue().initialTitle)
    }

    @Test
    fun `given deadline when set deadline id check current title in view state`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertEquals(deadline.title, viewModel.viewState.getOrAwaitValue().currentTitle)
    }

    @Test
    fun `given deadline when set deadline id check initial description in view state`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertEquals(deadline.description, viewModel.viewState.getOrAwaitValue().initialDescription)
    }

    @Test
    fun `given deadline when set deadline id check current description in view state`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertEquals(deadline.description, viewModel.viewState.getOrAwaitValue().currentDescription)
    }

    @Test
    fun `given deadline when set deadline id check no title error message is displayed`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertNull(viewModel.viewState.getOrAwaitValue().titleErrorMsg)
    }

    @Test
    fun `given prebuilt deadline when set deadline id check date edit not allowed`() {
        // given
        val deadline = generateDeadline().copy(deadlineType = DeadlineType.DAY_DEADLINE)
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertFalse(viewModel.viewState.getOrAwaitValue().allowEditDate)
    }

    @Test
    fun `given custom deadline when set deadline id check date edit allowed`() {
        // given
        val deadline = generateDeadline().copy(deadlineType = DeadlineType.CUSTOM)
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertTrue(viewModel.viewState.getOrAwaitValue().allowEditDate)
    }

    @Test
    fun `given custom deadline when set deadline id check start date`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertEquals(deadline.start, viewModel.viewState.getOrAwaitValue().startTime)
    }

    @Test
    fun `given custom deadline when set deadline id check start date display string`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertEquals(dateFormattedString, viewModel.viewState.getOrAwaitValue().startTimeString)
    }

    @Test
    fun `given custom deadline when set deadline id check end date`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertEquals(deadline.end, viewModel.viewState.getOrAwaitValue().endTime)
    }

    @Test
    fun `given custom deadline when set deadline id check end date display string`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertEquals(dateFormattedString, viewModel.viewState.getOrAwaitValue().endTimeString)
    }

    @Test
    fun `given deadline when set deadline id check color edit allowed`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertTrue(viewModel.viewState.getOrAwaitValue().allowEditColor)
    }

    @Test
    fun `given deadline when set deadline id check selected color same as given deadline`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertEquals(deadline.color, viewModel.viewState.getOrAwaitValue().selectedColor)
    }

    @Test
    fun `given deadline when set deadline id check notification edit allowed`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertTrue(viewModel.viewState.getOrAwaitValue().allowEditNotifications)
    }

    @Test
    fun `given deadline when set deadline id check notifications list same as given deadline`() {
        // given
        val deadline = generateDeadline()
        deadlineSubject.onNext(deadline)

        // when
        viewModel.setDeadlineId(deadline.id)

        // then
        assertEquals(
            deadline.notificationPercent,
            viewModel.viewState.getOrAwaitValue().notificationList
        )
    }

    @Test
    fun `given error loading deadline set deadline id check user message`() {
        // given
        whenever(deadlineRepo.observeDeadline(any()))
            .thenReturn(Flowable.error(Throwable(kFixture<String>())))

        // when
        viewModel.setDeadlineId(kFixture.range(1L..1000L))

        // then
        assertEquals(
            testString,
            (viewModel.singleViewState.getOrAwaitValue() as ShowUserMessage).message
        )
    }

    @Test
    fun `given error loading deadline set deadline id check screen closes`() {
        // given
        whenever(deadlineRepo.observeDeadline(any()))
            .thenReturn(Flowable.error(Throwable(kFixture<String>())))

        // when
        viewModel.setDeadlineId(kFixture.range(1L..1000L))

        // then
        assertTrue(
            (viewModel.singleViewState.getOrAwaitValue() as ShowUserMessage).closeScreen
        )
    }
}
