package com.kevalpatel2106.yip.detail

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.entity.Deadline
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyLong

@RunWith(JUnit4::class)
class DetailViewModelDeadlineMonitorTest : DetailViewModelTestSetUp() {

    @Test
    fun `given already monitoring when set deadline id with same id called check no duplicate monitoring`() {
        // given
        val deadline = generateDeadline()
        model.setDeadlineIdToMonitor(deadline.id)

        // when
        model.setDeadlineIdToMonitor(deadline.id)

        // check
        verify(deadlineRepo).observeDeadline(anyLong())
    }

    @Test
    fun `given deadline detail when set deadline id called check title text`() {
        // given
        val deadline = generateDeadline()
        deadlineObserver.onNext(deadline)

        // when
        model.setDeadlineIdToMonitor(deadline.id)

        // check
        val viewState = model.viewState.getOrAwaitValue()
        assertEquals(deadline.title, viewState.titleText)
    }

    @Test
    fun `given deadline detail when set deadline id called check show repeatable status`() {
        // given
        val deadline = generateDeadline()
        deadlineObserver.onNext(deadline)

        // when
        model.setDeadlineIdToMonitor(deadline.id)

        // check
        val viewState = model.viewState.getOrAwaitValue()
        assertTrue(viewState.showRepeatable)
    }

    @Test
    fun `given deadline detail when set deadline id called check start and end time`() {
        // given
        val deadline = generateDeadline()
        deadlineObserver.onNext(deadline)

        // when
        model.setDeadlineIdToMonitor(deadline.id)

        // check
        val viewState = model.viewState.getOrAwaitValue()
        assertEquals(formattedTime, viewState.startTimeText)
        assertEquals(formattedTime, viewState.endTimeText)
    }

    @Test
    fun `given deadline detail when set deadline id called check percent text`() {
        // given
        val deadline = generateDeadline()
        deadlineObserver.onNext(deadline)

        // when
        model.setDeadlineIdToMonitor(deadline.id)

        // check
        val viewState = model.viewState.getOrAwaitValue()
        assertEquals(deadlinePercentageString, viewState.percentText)
    }

    @Test
    fun `given deadline detail when set deadline id called check progress percent`() {
        // given
        val deadline = generateDeadline()
        deadlineObserver.onNext(deadline)

        // when
        model.setDeadlineIdToMonitor(deadline.id)

        // check
        val viewState = model.viewState.getOrAwaitValue()
        assertEquals(deadline.percent.toInt(), viewState.percent)
    }

    @Test
    fun `given deadline finished when set deadline id called check time left string is empty`() {
        // given
        val finishedDeadline = generateDeadline(101f)
        deadlineObserver.onNext(finishedDeadline)

        // when
        model.setDeadlineIdToMonitor(finishedDeadline.id)

        // check
        val viewState = model.viewState.getOrAwaitValue()
        assertTrue(viewState.timeLeftText.isEmpty())
    }

    @Test
    fun `given get deadline failed when set deadline id called check error message shown`() {
        // given
        val deadline = generateDeadline()
        whenever(deadlineRepo.observeDeadline(anyLong()))
            .thenReturn(Single.error<Deadline>(Throwable()).toFlowable())

        // when
        model.setDeadlineIdToMonitor(deadline.id)

        // check
        val singleEvent = model.singleEvent.getOrAwaitValue()
        assertEquals(deadlineOpenErrorMessage, (singleEvent as ShowUserMessage).message)
    }

    @Test
    fun `given get deadline failed when set deadline id called check screen close`() {
        // given
        val deadline = generateDeadline()
        whenever(deadlineRepo.observeDeadline(anyLong()))
            .thenReturn(Single.error<Deadline>(Throwable()).toFlowable())

        // when
        model.setDeadlineIdToMonitor(deadline.id)

        // check
        val singleEvent = model.singleEvent.getOrAwaitValue()
        assertTrue((singleEvent as ShowUserMessage).closeScreen)
    }
}
