package com.kevalpatel2106.yip.dashboard

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.dashboard.adapter.listItem.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.DeadlineListItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.EmptyRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ErrorRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.LoadingRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.PaddingItem
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
open class DashboardViewModelMonitorDeadlineTest : DashboardViewModelTestSetUp() {

    @Test
    fun `given user is pro and deadlines not empty when monitor deadlines called check deadline list items`() {
        // Given
        val testDeadlines = provideDeadlineList(6)
        deadlineListSubject.onNext(testDeadlines)
        isPurchasedSubject.onNext(true)

        // Check
        val deadlines = model.deadlines.getOrAwaitValue()
        testDeadlines.forEachIndexed { index, deadline ->
            assertEquals(deadline, (deadlines[index] as DeadlineListItem).deadline)
        }
    }

    @Test
    fun `given user is not pro and more than four deadlines when monitor deadlines called check 4th item is ad`() {
        // Given
        deadlineListSubject.onNext(provideDeadlineList(6))
        isPurchasedSubject.onNext(false)

        // Check
        val deadlines = model.deadlines.getOrAwaitValue()
        Assert.assertTrue(deadlines[4] is AdsItem)
    }

    @Test
    fun `given user is not pro and two deadlines when monitor deadlines called check second last item is ad`() {
        // Given
        deadlineListSubject.onNext(provideDeadlineList(3))
        isPurchasedSubject.onNext(false)

        // Check
        val deadlines = model.deadlines.getOrAwaitValue()
        Assert.assertTrue(deadlines[deadlines.size - 2] is AdsItem)
    }

    @Test
    fun `given user is pro deadlines not empty when monitor deadlines called check last item is padding`() {
        // Given
        deadlineListSubject.onNext(provideDeadlineList(6))
        isPurchasedSubject.onNext(true)

        // Check
        val deadlines = model.deadlines.getOrAwaitValue()
        Assert.assertTrue(deadlines.last() is PaddingItem)
    }

    @Test
    fun `given user is not pro deadlines not empty when monitor deadlines called check last item is padding`() {
        // Given
        deadlineListSubject.onNext(provideDeadlineList(6))
        isPurchasedSubject.onNext(false)

        // Check
        val deadlines = model.deadlines.getOrAwaitValue()
        Assert.assertTrue(deadlines.last() is PaddingItem)
    }

    @Test
    fun `given deadlines empty when monitor deadlines called check empty list item added`() {
        // Given
        deadlineListSubject.onNext(listOf())
        isPurchasedSubject.onNext(true)

        // Check
        val deadlines = model.deadlines.getOrAwaitValue()
        assertEquals(1, deadlines.size)
        assertEquals(noItemsMessage, (deadlines.last() as EmptyRepresentable).message)
    }

    @Test
    fun `given user is not pro and deadlines empty when monitor deadlines called check ads item not added`() {
        // Given
        deadlineListSubject.onNext(listOf())
        isPurchasedSubject.onNext(false)

        // Check
        val deadlines = model.deadlines.getOrAwaitValue()
        Assert.assertFalse(deadlines.contains(AdsItem))
    }

    @Test
    fun `given deadlines empty when monitor deadlines called check padding item not added`() {
        // Given
        deadlineListSubject.onNext(listOf())
        isPurchasedSubject.onNext(false)

        // Check
        val deadlines = model.deadlines.getOrAwaitValue()
        Assert.assertFalse(deadlines.contains(PaddingItem))
    }

    @Test
    fun `given monitor deadlines called when loading deadlines check loading item added`() {
        val deadlines = model.deadlines.getOrAwaitValue()

        // Given
        deadlineListSubject.onNext(provideDeadlineList(6))
        isPurchasedSubject.onNext(false)

        // Check
        assertEquals(1, deadlines.size)
        Assert.assertTrue(deadlines.contains(LoadingRepresentable))
    }

    @Test
    fun `given expanded deadline is not in deadline list when deadlines list updated check expanded view collapse`() {
        // given
        val testExpandedDeadline = kFixture<Long>()
        setAsDeadlineDetailExpanded(testExpandedDeadline)

        // when
        deadlineListSubject.onNext(provideDeadlineList(6))
        isPurchasedSubject.onNext(false)

        // Check
        val expandedViewState = model.expandViewState.getOrAwaitValue()
        assertEquals(DetailViewCollapsed, expandedViewState)
    }

    @Test
    fun `given expanded deadline is in deadline list when deadlines list updated check expanded deadline id not change`() {
        // given
        val deadlines = provideDeadlineList(6)
        val testExpandedDeadline = deadlines.first().id
        setAsDeadlineDetailExpanded(testExpandedDeadline)

        // when
        deadlineListSubject.onNext(deadlines)
        isPurchasedSubject.onNext(false)

        // Check
        val expandedViewState = model.expandViewState.getOrAwaitValue()
        assertEquals(testExpandedDeadline, (expandedViewState as DetailViewExpanded).deadlineId)
    }


    @Test
    fun `given error ocured while loading deadline when monitor deadlines called check error list item added`() {
        // Given
        Mockito.`when`(deadlineRepo.observeAllDeadlines())
            .thenReturn(
                PublishSubject.error<List<Deadline>>(Throwable()).hide()
                    .toFlowable(BackpressureStrategy.DROP)
            )
        isPurchasedSubject.onNext(false)
        model = DashboardViewModel(
            application,
            deadlineRepo,
            sharedPrefsProvider,
            billingRepo,
            appShortcutHelper
        )

        // Check
        val deadlines = model.deadlines.getOrAwaitValue()
        assertEquals(1, deadlines.size)
        assertEquals(
            deadlineMonitorErrorMessage,
            (deadlines.last() as ErrorRepresentable).message
        )
    }

    private fun provideDeadlineList(size: Int): MutableList<Deadline> {
        val list = mutableListOf<Deadline>()
        for (i in 1..size) {
            list.add(generateTestDeadline())
        }
        return list
    }

    private fun generateTestDeadline(): Deadline {
        return Deadline(
            id = kFixture(),
            title = kFixture(),
            color = DeadlineColor.COLOR_BLUE,
            end = Date(System.currentTimeMillis()),
            start = Date(
                System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                    1,
                    TimeUnit.DAYS
                )
            ),
            deadlineType = DeadlineType.DAY_DEADLINE,
            notificationPercent = arrayListOf(),
            percent = kFixture()
        )
    }
}
