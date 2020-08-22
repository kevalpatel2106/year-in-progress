package com.kevalpatel2106.yip.detail

import android.content.Intent
import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.generateDeadline
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DetailViewModelOptionMenuTest : DetailViewModelTestSetUp() {

    @Test
    fun `given deadline delete in progress when show option menu called check menu does not open`() {
        // given
        val deadline = generateDeadline(kFixture)
        model.setDeadlineIdToMonitor(deadline.id)
        whenever(deadlineRepo.deleteDeadline(deadline.id))
            .thenReturn(PublishSubject.create<Unit>().ignoreElements())
        model.onDeleteDeadlineConfirmed()

        // when
        model.showDetailOptionsMenu()

        // check
        assertNull(model.singleEvent.value)
    }

    @Test
    fun `given deadline delete not in progress when show option menu called check menu open`() {
        // given
        val deadline = generateDeadline(kFixture)
        model.setDeadlineIdToMonitor(deadline.id)

        // when
        model.showDetailOptionsMenu()

        // check
        assertEquals(OpenPopUpMenu, model.singleEvent.getOrAwaitValue())
    }

    @Test
    fun `given deadline detail set when delete deadline clicked check delete confirmation dialog appears`() {
        // given
        val deadline = generateDeadline(kFixture)
        model.setDeadlineIdToMonitor(deadline.id)
        deadlineObserver.onNext(deadline)

        // when
        model.onDeleteDeadlineClicked()

        // check
        val singleEvent = model.singleEvent.getOrAwaitValue()
        assertEquals(deadline.title, (singleEvent as ShowDeleteConfirmationDialog).deadlineTitle)
    }

    @Test
    fun `given deadline detail set when pin shortcut clicked check request pin shortcut initiated`() {
        // given
        val idCaptor = argumentCaptor<Long>()
        val titleCaptor = argumentCaptor<String>()
        val launchIntentCaptor = argumentCaptor<Intent>()
        val deadline = generateDeadline(kFixture)
        val launchIntent = Intent()
        whenever(appLaunchIntentProvider.launchAppWithDeadlineDetailIntent(context, deadline.id))
            .thenReturn(launchIntent)

        model.setDeadlineIdToMonitor(deadline.id)
        deadlineObserver.onNext(deadline)

        // when
        model.requestPinShortcut()

        // check
        verify(appShortcutHelper).requestPinShortcut(
            idCaptor.capture(),
            titleCaptor.capture(),
            launchIntentCaptor.capture()
        )
        assertEquals(deadline.id, idCaptor.lastValue)
        assertEquals(deadline.title, titleCaptor.lastValue)
        assertEquals(launchIntent, launchIntentCaptor.lastValue)
    }
}
