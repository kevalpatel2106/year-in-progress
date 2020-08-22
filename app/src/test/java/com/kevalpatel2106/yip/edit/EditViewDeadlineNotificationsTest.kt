package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.generateDeadline
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class EditViewDeadlineNotificationsTest : EditViewDeadlineModelTestSetUp() {

    private lateinit var deadline: Deadline
    private val notifications = listOf(0.4f, 23.5f, 76.2f)

    @Before
    fun setUp() {
        deadline = generateDeadline(kFixture).copy(notificationPercent = notifications)
        deadlineSubject.onNext(deadline)
        viewModel.setDeadlineId(deadline.id)
    }

    @Test
    fun `given user premium when add notification clicked check notification picker opens`() {
        // given
        viewModel.isPremiumUser = true

        // when
        viewModel.onAddNotificationClicked()

        // check
        assertEquals(ShowNotificationPicker, viewModel.singleViewState.getOrAwaitValue())
    }

    @Test
    fun `given user not premium when add notification clicked check payment screen opens`() {
        // given
        viewModel.isPremiumUser = false

        // when
        viewModel.onAddNotificationClicked()

        // check
        assertEquals(OpenPaymentScreen, viewModel.singleViewState.getOrAwaitValue())
    }

    @Test
    fun `when on new notification percent added check list updated`() {
        // when
        val newNotification = 56.6f
        viewModel.onNotificationAdded(newNotification)

        // check
        assertTrue(viewModel.viewState.getOrAwaitValue().notificationList.contains(newNotification))
        assertEquals(
            notifications.size + 1,
            viewModel.viewState.getOrAwaitValue().notificationList.size
        )
    }

    @Test
    fun `when on new notification percent which is already added once added again check list updated`() {
        // when
        val newNotification = notifications.first()
        viewModel.onNotificationAdded(newNotification)

        // check
        assertTrue(viewModel.viewState.getOrAwaitValue().notificationList.contains(newNotification))
        assertEquals(
            notifications.size,
            viewModel.viewState.getOrAwaitValue().notificationList.size
        )
    }

    @Test
    fun `when on new notification percent added check something changed`() {
        // when
        val newNotification = 56.6f
        viewModel.onNotificationAdded(newNotification)

        // check
        assertTrue(viewModel.isSomethingChanged)
    }

    @Test
    fun `when notification percent removed check list updated`() {
        // when
        val notificationToRemove = notifications.first()
        viewModel.onNotificationRemoved(notificationToRemove)

        // check
        assertFalse(
            viewModel.viewState.getOrAwaitValue().notificationList.contains(
                notificationToRemove
            )
        )
    }

    @Test
    fun `when notification percent removed check something changed`() {
        // when
        val notificationToRemove = 56.6f
        viewModel.onNotificationRemoved(notificationToRemove)

        // check
        assertTrue(viewModel.isSomethingChanged)
    }
}
