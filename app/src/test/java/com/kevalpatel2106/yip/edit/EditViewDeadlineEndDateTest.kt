package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.entity.Deadline
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
internal class EditViewDeadlineEndDateTest : EditViewDeadlineModelTestSetUp() {

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
        viewModel.onEndDateClicked()

        // check
        assertEquals(OpenEndDatePicker, viewModel.singleViewState.getOrAwaitValue())
    }

    @Test
    fun `given end date valid when end date selected check something change set to true`() {
        //given
        val testEndDate = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidEndDate(deadline.start, testEndDate)).thenReturn(true)

        // when
        viewModel.onEndDateSelected(testEndDate)

        // check
        Assert.assertTrue(viewModel.isSomethingChanged)
    }

    @Test
    fun `given end date valid when end date selected check end date updated in view state`() {
        //given
        val testEndDate = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidEndDate(deadline.start, testEndDate)).thenReturn(true)

        // when
        viewModel.onEndDateSelected(testEndDate)

        // check
        assertEquals(testEndDate, viewModel.viewState.getOrAwaitValue().endTime)
    }

    @Test
    fun `given end date valid when end date selected check end date display string updated`() {
        //given
        val testEndDate = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidEndDate(deadline.start, testEndDate)).thenReturn(true)

        // when
        viewModel.onEndDateSelected(testEndDate)

        // check
        verify(dateFormatter, times(1)).formatDateOnly(testEndDate)
    }

    @Test
    fun `given end date valid when end date selected check start date not changed`() {
        //given
        val testEndDate = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidEndDate(deadline.start, testEndDate)).thenReturn(true)

        // when
        viewModel.onEndDateSelected(testEndDate)

        // check
        assertEquals(deadline.start, viewModel.viewState.getOrAwaitValue().startTime)
    }

    @Test
    fun `given end date invalid when end date selected check user message displayed`() {
        //given
        val testEndDate = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidEndDate(deadline.start, testEndDate)).thenReturn(false)

        // when
        viewModel.onEndDateSelected(testEndDate)

        // check
        assertEquals(
            testString,
            (viewModel.singleViewState.getOrAwaitValue() as ShowUserMessage).message
        )
    }

    @Test
    fun `given end date invalid when end date selected check does not close screen`() {
        //given
        val testEndDate = Date(System.currentTimeMillis() + 10_000)
        whenever(validator.isValidEndDate(deadline.start, testEndDate)).thenReturn(false)

        // when
        viewModel.onEndDateSelected(testEndDate)

        // check
        assertFalse((viewModel.singleViewState.getOrAwaitValue() as ShowUserMessage).closeScreen)
    }
}
