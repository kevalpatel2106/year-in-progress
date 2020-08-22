package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.generateDeadline
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class EditViewDeadlineCloseButtonTest : EditViewDeadlineModelTestSetUp() {

    private lateinit var deadline: Deadline

    @Before
    fun setUp() {
        deadline = generateDeadline(kFixture)
        viewModel.setDeadlineId(deadline.id)
    }

    @Test
    fun `given somethings changed and not loading when on close press check confirmation dialog displayed`() {
        // given
        deadlineSubject.onNext(deadline)
        viewModel.onNotificationAdded(kFixture())

        // when
        viewModel.onClosePressed()

        // check
        assertEquals(ShowConfirmationDialog, viewModel.singleViewState.getOrAwaitValue())
    }

    @Test
    fun `given nothing changed and not loading when on close press check screen close`() {
        // given
        deadlineSubject.onNext(deadline)

        // when
        viewModel.onClosePressed()

        // check
        assertEquals(CloseScreen, viewModel.singleViewState.getOrAwaitValue())
    }

    @Test
    fun `given loading deadline when on close press check nothing happens`() {
        // when
        viewModel.onClosePressed()

        // check
        assertNull(viewModel.singleViewState.value)
    }

}
