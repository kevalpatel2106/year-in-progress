package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.core.setToDayMax
import com.kevalpatel2106.yip.core.setToDayMin
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.entity.isPreBuild
import com.kevalpatel2106.yip.notifications.DeadlineNotificationReceiver
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import java.util.Date

@RunWith(JUnit4::class)
internal class EditViewDeadlineSaveDeadlineTest : EditViewDeadlineModelTestSetUp() {

    private lateinit var deadline: Deadline
    private val addDeadlineResponseSubject = BehaviorSubject.create<Deadline>()

    @Before
    fun setUp() {
        deadline = generateDeadline()
        viewModel.setDeadlineId(deadline.id)
        whenever(
            deadlineRepo.addUpdateDeadline(
                anyLong(), anyString(), anyOrNull(), any(), any(), any(), any(), anyList()
            )
        ).thenReturn(addDeadlineResponseSubject.firstOrError())
    }

    @Test
    fun `given deadline is loading when save deadline check nothing happens`() {
        // when
        viewModel.saveDeadline()

        // check
        verify(deadlineRepo, never()).addUpdateDeadline(
            anyLong(), anyString(), anyOrNull(), any(), any(), any(), any(), anyList()
        )
    }

    @Test
    fun `given nothing changed when save deadline check nothing happens`() {
        // given
        deadlineSubject.onNext(deadline)

        // when
        viewModel.saveDeadline()

        // check
        verify(deadlineRepo, never()).addUpdateDeadline(
            anyLong(), anyString(), anyString(), any(), any(), any(), any(), anyList()
        )
    }

    @Test
    fun `given deadline title invalid when save deadline check title error message displayed`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        whenever(validator.isValidTitle(any())).thenReturn(false)
        whenever(validator.isValidStartDate(any(), any())).thenReturn(true)
        whenever(validator.isValidEndDate(any(), any())).thenReturn(true)
        whenever(validator.isValidDeadlineColor(anyInt())).thenReturn(true)
        whenever(validator.isValidNotification(anyList())).thenReturn(true)
        // when
        viewModel.saveDeadline()

        // check
        assertEquals(testString, viewModel.viewState.getOrAwaitValue().titleErrorMsg)
        verify(deadlineRepo, never()).addUpdateDeadline(
            anyLong(), anyString(), anyString(), any(), any(), any(), any(), anyList()
        )
    }

    @Test
    fun `given deadline start date invalid when save deadline check user message displayed`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        whenever(validator.isValidTitle(any())).thenReturn(true)
        whenever(validator.isValidStartDate(any(), any())).thenReturn(false)
        whenever(validator.isValidEndDate(any(), any())).thenReturn(true)
        whenever(validator.isValidDeadlineColor(anyInt())).thenReturn(true)
        whenever(validator.isValidNotification(anyList())).thenReturn(true)
        // when
        viewModel.saveDeadline()

        // check
        assertEquals(
            ShowUserMessage(testString, false),
            viewModel.singleViewState.getOrAwaitValue()
        )
        verify(deadlineRepo, never()).addUpdateDeadline(
            anyLong(), anyString(), anyString(), any(), any(), any(), any(), anyList()
        )
    }

    @Test
    fun `given deadline end date invalid when save deadline check user message displayed`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        whenever(validator.isValidTitle(any())).thenReturn(true)
        whenever(validator.isValidStartDate(any(), any())).thenReturn(true)
        whenever(validator.isValidEndDate(any(), any())).thenReturn(false)
        whenever(validator.isValidDeadlineColor(anyInt())).thenReturn(true)
        whenever(validator.isValidNotification(anyList())).thenReturn(true)

        // when
        viewModel.saveDeadline()

        // check
        assertEquals(
            ShowUserMessage(testString, false),
            viewModel.singleViewState.getOrAwaitValue()
        )
        verify(deadlineRepo, never()).addUpdateDeadline(
            anyLong(), anyString(), anyString(), any(), any(), any(), any(), anyList()
        )
    }

    @Test
    fun `given deadline color invalid when save deadline check user message displayed`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        whenever(validator.isValidTitle(any())).thenReturn(true)
        whenever(validator.isValidStartDate(any(), any())).thenReturn(true)
        whenever(validator.isValidEndDate(any(), any())).thenReturn(true)
        whenever(validator.isValidDeadlineColor(anyInt())).thenReturn(false)
        whenever(validator.isValidNotification(anyList())).thenReturn(true)

        // when
        viewModel.saveDeadline()

        // check
        assertEquals(
            ShowUserMessage(testString, false),
            viewModel.singleViewState.getOrAwaitValue()
        )
        verify(deadlineRepo, never()).addUpdateDeadline(
            anyLong(), anyString(), anyString(), any(), any(), any(), any(), anyList()
        )
    }

    @Test
    fun `given deadline notification invalid when save deadline check user message displayed`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        whenever(validator.isValidTitle(any())).thenReturn(true)
        whenever(validator.isValidStartDate(any(), any())).thenReturn(true)
        whenever(validator.isValidEndDate(any(), any())).thenReturn(true)
        whenever(validator.isValidDeadlineColor(anyInt())).thenReturn(true)
        whenever(validator.isValidNotification(anyList())).thenReturn(false)

        // when
        viewModel.saveDeadline()

        // check
        assertEquals(
            ShowUserMessage(testString, false),
            viewModel.singleViewState.getOrAwaitValue()
        )
        verify(deadlineRepo, never()).addUpdateDeadline(
            anyLong(), anyString(), anyString(), any(), any(), any(), any(), anyList()
        )
    }

    @Test
    fun `given all inputs valid when save deadline called check title to save`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        markAllInputValid()

        // when
        viewModel.saveDeadline()

        // check
        val titleCaptor = argumentCaptor<String>()
        verify(deadlineRepo).addUpdateDeadline(
            anyLong(), titleCaptor.capture(), anyOrNull(), any(), any(), any(), any(), anyList()
        )
        assertEquals(deadline.title.capitalize(), titleCaptor.lastValue)
    }

    @Test
    fun `given all inputs valid when save deadline called check description to save`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        markAllInputValid()

        // when
        viewModel.saveDeadline()

        // check
        val description = argumentCaptor<String>()
        verify(deadlineRepo).addUpdateDeadline(
            anyLong(), anyString(), description.capture(), any(), any(), any(), any(), anyList()
        )
        assertNull(description.lastValue)
    }

    @Test
    fun `given all inputs valid when save deadline called check deadline color to save`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        markAllInputValid()

        // when
        viewModel.saveDeadline()

        // check
        val colorCaptor = argumentCaptor<DeadlineColor>()
        verify(deadlineRepo).addUpdateDeadline(
            anyLong(), anyString(), anyOrNull(),
            colorCaptor.capture(), any(), any(), any(), anyList()
        )
        assertEquals(deadline.color, colorCaptor.lastValue)
    }

    @Test
    fun `given all inputs valid when save deadline called check start date to save`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        markAllInputValid()

        // when
        viewModel.saveDeadline()

        // check
        val startDateCaptor = argumentCaptor<Date>()
        verify(deadlineRepo).addUpdateDeadline(
            anyLong(), anyString(), anyOrNull(), any(),
            startDateCaptor.capture(), any(), any(), anyList()
        )
        assertEquals(deadline.start.apply { setToDayMin() }, startDateCaptor.lastValue)
    }

    @Test
    fun `given all inputs valid when save deadline called check end date to save`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        markAllInputValid()

        // when
        viewModel.saveDeadline()

        // check
        val endDateCaptor = argumentCaptor<Date>()
        verify(deadlineRepo).addUpdateDeadline(
            anyLong(), anyString(), anyOrNull(), any(),
            any(), endDateCaptor.capture(), any(), anyList()
        )
        assertEquals(deadline.start.apply { setToDayMax() }, endDateCaptor.lastValue)
    }

    @Test
    fun `given all inputs valid when save deadline called check deadline type to save`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        markAllInputValid()

        // when
        viewModel.saveDeadline()

        // check
        val typeCaptor = argumentCaptor<DeadlineType>()
        verify(deadlineRepo).addUpdateDeadline(
            anyLong(), anyString(), anyOrNull(), any(),
            any(), any(), typeCaptor.capture(), anyList()
        )
        assertEquals(deadline.deadlineType, typeCaptor.lastValue)
    }

    @Test
    fun `given all inputs valid when save deadline called check notification list to save`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        markAllInputValid()

        // when
        viewModel.saveDeadline()

        // check
        val notificationListCaptor = argumentCaptor<List<Float>>()
        verify(deadlineRepo).addUpdateDeadline(
            anyLong(), anyString(), anyOrNull(), any(),
            any(), any(), any(), notificationListCaptor.capture()
        )
        assertEquals(deadline.notificationPercent, notificationListCaptor.lastValue)
    }

    @Test
    fun `given all inputs valid when save deadline called check deadline is loading`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        markAllInputValid()

        // when
        viewModel.saveDeadline()

        // check
        with(viewModel.viewState.getOrAwaitValue()) {
            assertTrue(isLoading)
            assertFalse(allowEditNotifications)
            assertFalse(allowEditColor)
            assertFalse(allowEditDate)
        }
    }

    @Test
    fun `given all inputs valid when save deadline success check user message displayed `() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        markAllInputValid()
        addDeadlineResponseSubject.onNext(deadline)

        // when
        viewModel.saveDeadline()

        // check
        assertEquals(
            ShowUserMessage(testString, true),
            viewModel.singleViewState.getOrAwaitValue()
        )
    }

    @Test
    fun `given all inputs valid when save deadline success check alarms updated `() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        markAllInputValid()
        addDeadlineResponseSubject.onNext(deadline)

        // when
        viewModel.saveDeadline()

        // check
        verify(alarmRepo).updateAlarms(DeadlineNotificationReceiver::class.java)
    }

    @Test
    fun `given all inputs valid when save deadline error check user message displayed `() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        markAllInputValid()
        whenever(
            deadlineRepo.addUpdateDeadline(
                anyLong(), anyString(), anyOrNull(), any(), any(), any(), any(), anyList()
            )
        ).thenReturn(Single.error(Throwable(kFixture<String>())))

        // when
        viewModel.saveDeadline()

        // check
        assertEquals(
            ShowUserMessage(testString, false),
            viewModel.singleViewState.getOrAwaitValue()
        )
    }

    @Test
    fun `given all inputs valid when save deadline error check view state changed to not loading `() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.isSomethingChanged = true
        markAllInputValid()
        whenever(
            deadlineRepo.addUpdateDeadline(
                anyLong(), anyString(), anyOrNull(), any(), any(), any(), any(), anyList()
            )
        ).thenReturn(Single.error(Throwable(kFixture<String>())))

        // when
        viewModel.saveDeadline()

        // check
        with(viewModel.viewState.getOrAwaitValue()) {
            assertFalse(isLoading)
            assertTrue(allowEditNotifications)
            assertTrue(allowEditColor)
            assertEquals(!deadline.deadlineType.isPreBuild(), allowEditDate)
        }
    }

    private fun markAllInputValid() {
        whenever(validator.isValidTitle(any())).thenReturn(true)
        whenever(validator.isValidStartDate(any(), any())).thenReturn(true)
        whenever(validator.isValidEndDate(any(), any())).thenReturn(true)
        whenever(validator.isValidDeadlineColor(anyInt())).thenReturn(true)
        whenever(validator.isValidNotification(anyList())).thenReturn(true)
    }

}
