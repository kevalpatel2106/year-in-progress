package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.entity.Deadline
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

@RunWith(JUnit4::class)
internal class EditViewDeadlineDateRangeTest : EditViewDeadlineModelTestSetUp() {
    private lateinit var deadline: Deadline
    private lateinit var startDateTest: Date
    private lateinit var endDateTest: Date

    @Before
    fun setUp() {
        deadline = generateDeadline()
        deadlineSubject.onNext(deadline)
        viewModel.setDeadlineId(deadline.id)

        startDateTest = Date(System.currentTimeMillis() + 10_000)
        endDateTest = Date(startDateTest.time + 10_000)
    }

    @Test
    fun `when set start date clicked check date picker for start date opened`() {
        // when
        viewModel.onDateClicked()

        // check
        assertEquals(OpenDatePicker, viewModel.singleViewState.getOrAwaitValue())
    }

    @Test
    fun `given start and end date valid when start date selected check start date updated in view state`() {
        //given
        whenever(validator.isValidStartDate(startDateTest, endDateTest)).thenReturn(true)
        whenever(validator.isValidEndDate(startDateTest, endDateTest)).thenReturn(true)

        // when
        viewModel.onDateRangeSelected(startDateTest, endDateTest)

        // check
        assertEquals(startDateTest, viewModel.viewState.getOrAwaitValue().startTime)
    }

    @Test
    fun `given start and end date valid when start date selected check start date display string updated`() {
        //given
        whenever(validator.isValidStartDate(startDateTest, endDateTest)).thenReturn(true)
        whenever(validator.isValidEndDate(startDateTest, endDateTest)).thenReturn(true)

        // when
        viewModel.onDateRangeSelected(startDateTest, endDateTest)

        // check
        verify(dateFormatter, times(1)).formatDateOnly(startDateTest)
    }

    @Test
    fun `given start and end date valid when end date selected check end date updated in view state`() {
        //given
        whenever(validator.isValidStartDate(startDateTest, endDateTest)).thenReturn(true)
        whenever(validator.isValidEndDate(startDateTest, endDateTest)).thenReturn(true)

        // when
        viewModel.onDateRangeSelected(startDateTest, endDateTest)

        // check
        assertEquals(endDateTest, viewModel.viewState.getOrAwaitValue().endTime)
    }

    @Test
    fun `given start and end date valid when end date selected check end date display string updated`() {
        //given
        whenever(validator.isValidStartDate(startDateTest, endDateTest)).thenReturn(true)
        whenever(validator.isValidEndDate(startDateTest, endDateTest)).thenReturn(true)

        // when
        viewModel.onDateRangeSelected(startDateTest, endDateTest)

        // check
        verify(dateFormatter, times(1)).formatDateOnly(endDateTest)
    }

    @Test
    fun `given start and end date valid when end date selected check is something changed true`() {
        //given
        whenever(validator.isValidStartDate(startDateTest, endDateTest)).thenReturn(true)
        whenever(validator.isValidEndDate(startDateTest, endDateTest)).thenReturn(true)

        // when
        viewModel.onDateRangeSelected(startDateTest, endDateTest)

        // check
        assertTrue(viewModel.isSomethingChanged)
    }

    @Test
    fun `given start date invalid when start date selected check start date not updated in view state`() {
        //given
        whenever(validator.isValidStartDate(startDateTest, endDateTest)).thenReturn(false)
        whenever(validator.isValidEndDate(startDateTest, endDateTest)).thenReturn(true)

        // when
        viewModel.onDateRangeSelected(startDateTest, endDateTest)

        // check
        assertEquals(
            ShowUserMessage(testString, false),
            viewModel.singleViewState.getOrAwaitValue()
        )
    }

    @Test
    fun `given end date invalid when start date selected check user message shown`() {
        //given
        whenever(validator.isValidStartDate(startDateTest, endDateTest)).thenReturn(true)
        whenever(validator.isValidEndDate(startDateTest, endDateTest)).thenReturn(false)

        // when
        viewModel.onDateRangeSelected(startDateTest, endDateTest)

        // check
        assertEquals(
            ShowUserMessage(testString, false),
            viewModel.singleViewState.getOrAwaitValue()
        )
    }
}
