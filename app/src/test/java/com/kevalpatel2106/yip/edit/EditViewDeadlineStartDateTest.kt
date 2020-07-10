package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.entity.Deadline
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
internal class EditViewDeadlineStartDateTest : EditViewDeadlineModelTestSetUp() {
    private lateinit var deadline: Deadline

    @Before
    fun setUp() {
        deadline = generateDeadline()
        deadlineSubject.onNext(deadline)
        viewModel.setDeadlineId(deadline.id)
    }

    @Test
    fun `when set start date clicked check date picker for start date opened`() {
        // when
        viewModel.onStartDateClicked()

        // check
        assertEquals(OpenStartDatePicker, viewModel.singleViewState.getOrAwaitValue())
    }

    @Test
    fun `given start date valid when start date selected check start date updated in view state`() {
        //given
        val startDateTest = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidStartDate(startDateTest, deadline.end)).thenReturn(true)

        // when
        viewModel.onStartDateSelected(startDateTest)

        // check
        assertEquals(startDateTest, viewModel.viewState.getOrAwaitValue().startTime)
    }

    @Test
    fun `given start date valid when start date selected check start date display string updated`() {
        //given
        val startDateTest = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidStartDate(startDateTest, deadline.end)).thenReturn(true)

        // when
        viewModel.onStartDateSelected(startDateTest)

        // check
        verify(dateFormatter, times(1)).formatDateOnly(startDateTest)
    }

    @Test
    fun `given start date valid when start date selected check end date does not change`() {
        //given
        val startDateTest = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidStartDate(startDateTest, deadline.end)).thenReturn(true)

        // when
        viewModel.onStartDateSelected(startDateTest)

        // check
        assertEquals(deadline.end, viewModel.viewState.getOrAwaitValue().endTime)
    }

    @Test
    fun `given start date valid when start date selected check end date string does not change`() {
        //given
        val startDateTest = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidStartDate(startDateTest, deadline.end)).thenReturn(true)

        // when
        viewModel.onStartDateSelected(startDateTest)

        // check
        val endDateCapture = argumentCaptor<Date>()
        verify(dateFormatter, atLeast(1)).formatDateOnly(endDateCapture.capture())
        assertEquals(deadline.end, endDateCapture.lastValue)
    }

    @Test
    fun `given start date invalid when start date selected check start date updated in view state`() {
        //given
        val startDateTest = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidStartDate(startDateTest, deadline.end)).thenReturn(false)

        // when
        viewModel.onStartDateSelected(startDateTest)

        // check
        assertEquals(startDateTest, viewModel.viewState.getOrAwaitValue().startTime)
    }

    @Test
    fun `given start date invalid when start date selected check end date changed to one day after start date`() {
        //given
        val startDateTest = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidStartDate(startDateTest, deadline.end)).thenReturn(false)

        // when
        viewModel.onStartDateSelected(startDateTest)

        // check
        assertEquals(
            startDateTest.time + TimeUnit.DAYS.toMillis(1),
            viewModel.viewState.getOrAwaitValue().endTime.time
        )
    }

    @Test
    fun `given start date invalid when start date selected check end date string changed to one day after start date`() {
        //given
        val startDateTest = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidStartDate(startDateTest, deadline.end)).thenReturn(false)

        // when
        viewModel.onStartDateSelected(startDateTest)

        // check
        val endDateCapture = argumentCaptor<Date>()
        verify(dateFormatter, atLeast(1)).formatDateOnly(endDateCapture.capture())
        assertEquals(
            startDateTest.time + TimeUnit.DAYS.toMillis(1),
            endDateCapture.lastValue.time
        )
    }

    @Test
    fun `given start date when start date selected check something change set to true`() {
        //given
        val startDateTest = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidStartDate(startDateTest, deadline.end)).thenReturn(kFixture())

        // when
        viewModel.onStartDateSelected(startDateTest)

        // check
        assertTrue(viewModel.isSomethingChanged)
    }
}
