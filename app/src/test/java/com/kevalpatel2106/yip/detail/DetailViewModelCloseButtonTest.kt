package com.kevalpatel2106.yip.detail

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyLong

@RunWith(JUnit4::class)
class DetailViewModelCloseButtonTest : DetailViewModelTestSetUp() {

    @Test
    fun `given deadline delete in progress when close detail called check detail does not close`() {
        // given
        whenever(deadlineRepo.deleteDeadline(anyLong()))
            .thenReturn(PublishSubject.create<Unit>().ignoreElements())
        model.onDeleteDeadlineConfirmed()

        // when
        model.onCloseButtonClicked()

        // check
        assertNull(model.singleEvent.value)
    }

    @Test
    fun `given deadline delete not in progress when close detail called check screen closes`() {
        // when
        model.onCloseButtonClicked()

        // check
        assertEquals(CloseDetailScreen, model.singleEvent.getOrAwaitValue())
    }

}
