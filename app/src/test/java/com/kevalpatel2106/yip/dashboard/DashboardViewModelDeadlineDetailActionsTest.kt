package com.kevalpatel2106.yip.dashboard

import com.kevalpatel2106.testutils.getOrAwaitValue
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

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
        Mockito.`when`(deadlineRepo.isDeadlineExist(testExpandedDeadline))
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
        Mockito.`when`(deadlineRepo.isDeadlineExist(testExpandedDeadline))
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
        Mockito.`when`(deadlineRepo.isDeadlineExist(testExpandedDeadline))
            .thenReturn(Single.just(false))

        // when
        model.onOpenDeadlineDetail(testExpandedDeadline)

        // Check
        val singleEvent = model.singleEvents.getOrAwaitValue()
        assertEquals(deadlineNotFoundMessage, (singleEvent as ShowUserMessage).message)
    }

    @Test
    fun `given never ask for rating set when open deadline  with rating number check ask rating dialog does not appear`() {
        // given
        val deadlineId = kFixture<Long>()
        Mockito.`when`(deadlineRepo.isDeadlineExist(deadlineId))
            .thenReturn(Single.just(true))
        Mockito.`when`(sharedPrefsProvider.getBoolFromPreference(DashboardViewModel.PREF_KEY_NEVER_ASK_RATE))
            .thenReturn(true)

        // when
        model.onOpenDeadlineDetail(deadlineId, DashboardViewModel.RANDOM_NUMBER_FOR_RATING)

        // Check
        assertNull(model.singleEvents.value)
    }

    @Test
    fun `given never ask for rating set to false when open deadline with rating number check ask rating dialog shows`() {
        // given
        val deadlineId = kFixture<Long>()
        Mockito.`when`(deadlineRepo.isDeadlineExist(deadlineId))
            .thenReturn(Single.just(true))
        Mockito.`when`(sharedPrefsProvider.getBoolFromPreference(DashboardViewModel.PREF_KEY_NEVER_ASK_RATE))
            .thenReturn(false)

        // when
        model.onOpenDeadlineDetail(deadlineId, DashboardViewModel.RANDOM_NUMBER_FOR_RATING)

        // Check
        val singleEvent = model.singleEvents.getOrAwaitValue()
        assertTrue(singleEvent is AskForRating)
    }

    @Test
    fun `given never ask for rating set to false when open deadline with non-rating number check ask rating dialog not shown`() {
        // given
        val deadlineId = kFixture<Long>()
        Mockito.`when`(deadlineRepo.isDeadlineExist(deadlineId))
            .thenReturn(Single.just(true))
        Mockito.`when`(sharedPrefsProvider.getBoolFromPreference(DashboardViewModel.PREF_KEY_NEVER_ASK_RATE))
            .thenReturn(false)

        // when
        model.onOpenDeadlineDetail(deadlineId, 234)

        // Check
        assertNull(model.singleEvents.value)
    }

    @Test
    fun `given user is premium when open deadline with ads number check interstitial ad never appear`() {
        // given
        val deadlineId = kFixture<Long>()
        Mockito.`when`(deadlineRepo.isDeadlineExist(deadlineId))
            .thenReturn(Single.just(true))
        Mockito.`when`(billingRepo.isPurchased()).thenReturn(true)

        // when
        model.onOpenDeadlineDetail(deadlineId, DashboardViewModel.RANDOM_NUMBER_FOR_AD)

        // Check
        assertNull(model.singleEvents.value)
    }

    @Test
    fun `given user is not premium when open deadline with ads number check ad appears`() {
        // given
        val deadlineId = kFixture<Long>()
        Mockito.`when`(deadlineRepo.isDeadlineExist(deadlineId))
            .thenReturn(Single.just(true))
        Mockito.`when`(billingRepo.isPurchased()).thenReturn(false)

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
        Mockito.`when`(deadlineRepo.isDeadlineExist(deadlineId))
            .thenReturn(Single.just(true))
        Mockito.`when`(billingRepo.isPurchased()).thenReturn(false)

        // when
        model.onOpenDeadlineDetail(deadlineId, 346)

        // Check
        assertNull(model.singleEvents.value)
    }
}
