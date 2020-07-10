package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.testutils.getOrAwaitValue
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class EditViewDeadlineMiscTest : EditViewDeadlineModelTestSetUp() {

    @Test
    fun `when view model initialised check initial view state`() {
        // check
        assertEquals(
            EditViewState.initialState(dateFormatter),
            viewModel.viewState.getOrAwaitValue()
        )
    }

}
