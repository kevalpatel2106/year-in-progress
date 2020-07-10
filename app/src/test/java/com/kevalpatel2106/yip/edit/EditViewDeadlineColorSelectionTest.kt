package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class EditViewDeadlineColorSelectionTest : EditViewDeadlineModelTestSetUp() {

    private lateinit var deadline: Deadline

    @Before
    fun setUp() {
        deadline = generateDeadline()
        deadlineSubject.onNext(deadline)
        viewModel.setDeadlineId(deadline.id)
    }

    @Test
    fun `given user is not premium when color changed check payment screen opens`() {
        // given
        viewModel.isPremiumUser = false
        whenever(validator.isValidDeadlineColor(anyInt())).thenReturn(kFixture())

        // when
        viewModel.onColorSelected(kFixture())

        // check
        assertEquals(OpenPaymentScreen, viewModel.singleViewState.getOrAwaitValue())
    }

    @Test
    fun `given user is not premium when color changed check something changed flag is false`() {
        // given
        viewModel.isPremiumUser = false
        whenever(validator.isValidDeadlineColor(anyInt())).thenReturn(kFixture())

        // when
        viewModel.onColorSelected(kFixture())

        // check
        assertFalse(viewModel.isSomethingChanged)
    }

    @Test
    fun `given user is premium and color is valid when color changed check selected color updates in view state`() {
        // given
        viewModel.isPremiumUser = true
        whenever(validator.isValidDeadlineColor(anyInt())).thenReturn(true)
        val selectedColor = DeadlineColor.COLOR_PINK

        // when
        viewModel.onColorSelected(selectedColor.colorInt)

        // check
        assertEquals(selectedColor, viewModel.viewState.getOrAwaitValue().selectedColor)
    }

    @Test
    fun `given user is premium and color is valid when color changed check something changed set to true`() {
        // given
        viewModel.isPremiumUser = true
        whenever(validator.isValidDeadlineColor(anyInt())).thenReturn(true)
        val selectedColor = DeadlineColor.COLOR_PINK

        // when
        viewModel.onColorSelected(selectedColor.colorInt)

        // check
        assertTrue(viewModel.isSomethingChanged)
    }

    @Test
    fun `given user is premium and color is invalid when color changed check user message displayed`() {
        // given
        viewModel.isPremiumUser = true
        whenever(validator.isValidDeadlineColor(anyInt())).thenReturn(false)
        val selectedColor = DeadlineColor.COLOR_PINK

        // when
        viewModel.onColorSelected(selectedColor.colorInt)

        // check
        assertEquals(
            testString,
            (viewModel.singleViewState.getOrAwaitValue() as ShowUserMessage).message
        )
    }

    @Test
    fun `given user is premium and color is invalid when color changed check does not close screen`() {
        // given
        viewModel.isPremiumUser = true
        whenever(validator.isValidDeadlineColor(anyInt())).thenReturn(false)
        val selectedColor = DeadlineColor.COLOR_PINK

        // when
        viewModel.onColorSelected(selectedColor.colorInt)

        // check
        assertFalse((viewModel.singleViewState.getOrAwaitValue() as ShowUserMessage).closeScreen)
    }
}
