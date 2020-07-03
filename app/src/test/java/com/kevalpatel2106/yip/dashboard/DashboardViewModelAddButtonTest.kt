package com.kevalpatel2106.yip.dashboard

import com.kevalpatel2106.testutils.getOrAwaitValue
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DashboardViewModelAddButtonTest : DashboardViewModelTestSetUp() {

    @Test
    fun `given deadline is expanded when add button clicked check edit deadline screen opens`() {
        // given
        val testExpandedDeadline = kFixture<Long>()
        setAsDeadlineDetailExpanded(testExpandedDeadline)

        // when
        model.onAddNewButtonClicked()

        // Check
        val singleEvent = model.singleEvents.getOrAwaitValue()
        Assert.assertEquals(testExpandedDeadline, (singleEvent as OpenEditDeadline).deadlineId)
    }

    @Test
    fun `given deadline is not expanded when add button clicked check create new deadline screen opens`() {
        // given
        model.onCloseDeadlineDetail()

        // when
        model.onAddNewButtonClicked()

        // Check
        val singleEvent = model.singleEvents.getOrAwaitValue()
        Assert.assertEquals(OpenCreateNewDeadline, singleEvent)
    }
}
