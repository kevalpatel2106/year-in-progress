package com.kevalpatel2106.yip.notifications

import com.kevalpatel2106.testutils.getKFixture
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.generateDeadline
import com.kevalpatel2106.yip.repo.deadlineRepo.DeadlineRepo
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class DeadlineNotificationReceiverHelperTest {

    @Mock
    internal lateinit var deadlineRepo: DeadlineRepo

    @Mock
    internal lateinit var notificationHandler: DeadlineNotificationHandler

    private val kFixture = getKFixture()
    private lateinit var receiverHelper: DeadlineNotificationReceiverHelper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        receiverHelper = DeadlineNotificationReceiverHelper(deadlineRepo, notificationHandler)
    }

    @Test
    fun `given deadline percent in tolerance when on receive called notification displayed`() {
        // given
        val percent = 34.3f
        val deadline = generateDeadline(kFixture, percent, listOf(percent + 1f))
        whenever(deadlineRepo.observeDeadline(deadline.id))
            .thenReturn(Single.just(deadline).toFlowable())

        // when
        receiverHelper.onReceive(deadline.id)

        // then
        verify(notificationHandler).notify(deadline)
    }

    @Test
    fun `given deadline percent out of tolerance when on receive called notification not displayed`() {
        // given
        val percent = 34.3f
        val deadline = generateDeadline(kFixture, percent, listOf(percent + 1.01f))
        whenever(deadlineRepo.observeDeadline(deadline.id))
            .thenReturn(Single.just(deadline).toFlowable())

        // when
        receiverHelper.onReceive(deadline.id)

        // then
        verify(notificationHandler, never()).notify(any())
    }

    @Test
    fun `given error getting deadline when on receive called notification not displayed`() {
        // given
        val deadlineId = kFixture<Long>()
        whenever(deadlineRepo.observeDeadline(deadlineId))
            .thenReturn(Single.error<Deadline>(Throwable()).toFlowable())

        // when
        receiverHelper.onReceive(deadlineId)

        // then
        verify(notificationHandler, never()).notify(any())
    }
}
