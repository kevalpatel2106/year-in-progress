package com.kevalpatel2106.yip.dashboard

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.core.AppConstants
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DashboardViewModelDeadlineDetailActionsTest : DashboardViewModelTestSetUp() {

    @Test
    fun `when view model created check initial value of expanded view state is collapsed`() {
        assertEquals(DetailViewCollapsed, model.expandViewState.value)
    }

    @Test
    fun `given deadline is expanded when close deadline called check expanded view state is collapsed`() {
        // given
        val testExpandedDeadline = kFixture<Long>()
        setAsDeadlineDetailExpanded(testExpandedDeadline)

        // when
        model.onCloseDeadlineDetail()

        // Check
        assertEquals(DetailViewCollapsed, model.expandViewState.getOrAwaitValue())
    }

    @Test
    fun `given deadline to expand exist when open deadline called check expanded detail id changes`() {
        // given
        val testExpandedDeadline = kFixture<Long>()
        whenever(deadlineRepo.isDeadlineExist(testExpandedDeadline))
            .thenReturn(Single.just(true))

        // when
        model.onOpenDeadlineDetail(testExpandedDeadline)

        // Check
        val expandedViewState = model.expandViewState.getOrAwaitValue()
        assertEquals(testExpandedDeadline, (expandedViewState as DetailViewExpanded).deadlineId)
    }

    @Test
    fun `given is deadline exist check fails when open deadline called check user message shown`() {
        // given
        val testExpandedDeadline = kFixture<Long>()
        whenever(deadlineRepo.isDeadlineExist(testExpandedDeadline))
            .thenReturn(Single.error(Throwable()))

        // when
        model.onOpenDeadlineDetail(testExpandedDeadline)

        // Check
        val singleEvent = model.singleEvents.getOrAwaitValue()
        assertEquals(deadlineNotFoundMessage, (singleEvent as ShowUserMessage).message)
    }

    @Test
    fun `given deadline to expand does not exist when open deadline called check error message shown`() {
        // given
        val testExpandedDeadline = kFixture<Long>()
        whenever(deadlineRepo.isDeadlineExist(testExpandedDeadline))
            .thenReturn(Single.just(false))

        // when
        model.onOpenDeadlineDetail(testExpandedDeadline)

        // Check
        val singleEvent = model.singleEvents.getOrAwaitValue()
        assertEquals(deadlineNotFoundMessage, (singleEvent as ShowUserMessage).message)
    }

    @Test
    fun `given user is premium when open deadline with ads number check interstitial ad never appear`() {
        // given
        val deadlineId = kFixture<Long>()
        whenever(deadlineRepo.isDeadlineExist(deadlineId))
            .thenReturn(Single.just(true))
        whenever(billingRepo.isPurchased()).thenReturn(true)

        // when
        model.onOpenDeadlineDetail(deadlineId, DashboardViewModel.RANDOM_NUMBER_FOR_AD)

        // Check
        assertNull(model.singleEvents.value)
    }

    @Test
    fun `given user is not premium when open deadline with ads number check ad appears`() {
        // given
        val deadlineId = kFixture<Long>()
        whenever(deadlineRepo.isDeadlineExist(deadlineId))
            .thenReturn(Single.just(true))
        whenever(billingRepo.isPurchased()).thenReturn(false)

        // when
        model.onOpenDeadlineDetail(deadlineId, DashboardViewModel.RANDOM_NUMBER_FOR_AD)

        // Check
        val singleEvent = model.singleEvents.getOrAwaitValue()
        assertTrue(singleEvent is ShowInterstitialAd)
    }

    @Test
    fun `given user is not premium when open deadline with non-ads number check ad not shown`() {
        // given
        val deadlineId = kFixture<Long>()
        whenever(deadlineRepo.isDeadlineExist(deadlineId))
            .thenReturn(Single.just(true))
        whenever(billingRepo.isPurchased()).thenReturn(false)

        // when
        model.onOpenDeadlineDetail(deadlineId, 346)

        // Check
        assertNull(model.singleEvents.value)
    }

    @Test
    fun `given deadline id to open is invalid when open deadline called check deadline is not open`() {
        // given
        val deadlineIdToOpen = AppConstants.INVALID_DEADLINE_ID

        // when
        model.onOpenDeadlineDetail(deadlineIdToOpen, kFixture())

        // Check
        verify(deadlineRepo, never()).isDeadlineExist(deadlineIdToOpen)
        assertNull(model.singleEvents.value)
    }
}
