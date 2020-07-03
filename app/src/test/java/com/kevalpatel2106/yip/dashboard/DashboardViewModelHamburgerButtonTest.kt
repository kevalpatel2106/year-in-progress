package com.kevalpatel2106.yip.dashboard

import com.kevalpatel2106.testutils.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DashboardViewModelHamburgerButtonTest : DashboardViewModelTestSetUp() {

    @Test
    fun `given deadline not expanded when hamburger button clicked check bottom navigation opens`() {
        // given
        model.onCloseDeadlineDetail()

        // when
        model.onHamburgerMenuClicked()

        // Check
        val singleEvent = model.singleEvents.getOrAwaitValue()
        assertEquals(OpenBottomNavigationSheet, singleEvent)
    }

    @Test
    fun `given deadline expanded when hamburger button clicked check nothing happens`() {
        // given
        setAsDeadlineDetailExpanded(kFixture())

        // when
        model.onHamburgerMenuClicked()

        // Check
        assertNull(model.singleEvents.value)
    }
}
