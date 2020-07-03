package com.kevalpatel2106.yip.dashboard

import com.kevalpatel2106.testutils.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DashboardViewModelBackButtonTest : DashboardViewModelTestSetUp() {

    @Test
    fun `given deadline expanded when back pressed check expanded deadline id resets`() {
        // given
        setAsDeadlineDetailExpanded(kFixture())

        // when
        model.onBackPressed()

        // Check
        assertEquals(DetailViewCollapsed, model.expandViewState.getOrAwaitValue())
    }

    @Test
    fun `given deadline not expanded when back pressed check screen closes`() {
        // given
        model.onCloseDeadlineDetail()

        // when
        model.onBackPressed()

        // Check
        val singleEvent = model.singleEvents.getOrAwaitValue()
        assertEquals(CloseScreen, singleEvent)
    }
}
