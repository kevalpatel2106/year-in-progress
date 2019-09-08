package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.yip.core.setToDayMax
import com.kevalpatel2106.yip.core.setToDayMin
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    private val progressType = ProgressType.CUSTOM
    private val progressColor = ProgressColor.COLOR_GRAY
    private val notificationList = arrayListOf(1F, 3F)
    private val progressStartTime = Date().apply { setToDayMin() }
    private val progressEndTime = Date().apply { setToDayMax() }

    private val state = EditViewState(
        isLoadingProgress = true,
        isSomethingChanged = false,

        progressType = progressType,

        initialTitle = initialTitle,
        currentTitle = currentTitle,
        titleErrorMsg = titleErrorMsg,

        allowEditDate = true,
        progressStartTime = progressStartTime,
        progressEndTime = progressEndTime,

        progressColor = progressColor,
        lockColorPicker = true,

        lockNotification = true,
        notificationList = notificationList
    )

    @Test
    fun checkInitialState() {
        assertTrue(state.isLoadingProgress)
        assertFalse(state.isSomethingChanged)
        assertEquals(progressType, state.progressType)
        assertEquals(initialTitle, state.initialTitle)
        assertEquals(currentTitle, state.currentTitle)
        assertEquals(titleErrorMsg, state.titleErrorMsg)
        assertTrue(state.allowEditDate)
        assertEquals(progressStartTime, state.progressStartTime)
        assertEquals(progressEndTime, state.progressEndTime)
        assertEquals(progressColor, state.progressColor)
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
}
