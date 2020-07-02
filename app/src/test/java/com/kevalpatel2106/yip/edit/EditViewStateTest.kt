package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.yip.core.setToDayMax
import com.kevalpatel2106.yip.core.setToDayMin
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
class EditViewStateTest {
    private val initialTitle = "initial title"
    private val currentTitle = "current title"
    private val titleErrorMsg = "error title"
    private val type = DeadlineType.CUSTOM
    private val color = DeadlineColor.COLOR_GRAY
    private val notificationList = arrayListOf(1F, 3F)
    private val startTime = Date().apply { setToDayMin() }
    private val endTime = Date().apply { setToDayMax() }

    private val state = EditViewState(
        isLoading = true,
        isSomethingChanged = false,

        type = type,

        initialTitle = initialTitle,
        currentTitle = currentTitle,
        titleErrorMsg = titleErrorMsg,

        allowEditDate = true,
        startTime = startTime,
        endTime = endTime,

        selectedColor = color,
        lockColorPicker = true,

        lockNotification = true,
        notificationList = notificationList
    )

    @Test
    fun checkInitialState() {
        assertTrue(state.isLoading)
        assertFalse(state.isSomethingChanged)
        assertEquals(type, state.type)
        assertEquals(initialTitle, state.initialTitle)
        assertEquals(currentTitle, state.currentTitle)
        assertEquals(titleErrorMsg, state.titleErrorMsg)
        assertTrue(state.allowEditDate)
        assertEquals(startTime, state.startTime)
        assertEquals(endTime, state.endTime)
        assertEquals(color, state.selectedColor)
        assertTrue(state.lockColorPicker)
        assertTrue(state.lockNotification)
        assertEquals(notificationList.size, state.notificationList.size)
    }

    @Test
    fun checkIsTitleChangedWhenCurrentTitleDifferent() {
        val state = state.copy(initialTitle = initialTitle, currentTitle = currentTitle)
        assertTrue(state.isTitleChanged())
    }

    @Test
    fun checkIsTitleChangedWhenCurrentTitleSame() {
        val state = state.copy(initialTitle = initialTitle, currentTitle = initialTitle)
        assertFalse(state.isTitleChanged())
    }

    @Test
    fun checkIsTitleChangedWhenCurrentTitleWithWhiteSpaces() {
        val state = state.copy(initialTitle = initialTitle, currentTitle = "$initialTitle  ")
        assertFalse(state.isTitleChanged())

        val state1 = state.copy(initialTitle = "$currentTitle  ", currentTitle = currentTitle)
        assertFalse(state1.isTitleChanged())
    }

    @Test
    fun checkEquals() {
        val withAnotherTitle = state.copy(initialTitle = "xyz new")
        val withLoadingStatus = state.copy(isLoading = false)
        val sameObject = state.copy()

        assertEquals(sameObject, state)
        assertNotEquals(withAnotherTitle, state)
        assertNotEquals(withLoadingStatus, state)
    }

    @Test
    fun checkHashcode() {
        val withAnotherTitle = state.copy(initialTitle = "xyz new")
        val withLoadingStatus = state.copy(isLoading = false)
        val sameObject = state.copy()

        assertEquals(sameObject.hashCode(), state.hashCode())
        assertNotEquals(withAnotherTitle.hashCode(), state.hashCode())
        assertNotEquals(withLoadingStatus.hashCode(), state.hashCode())
    }
}
