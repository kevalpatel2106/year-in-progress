package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.yip.core.emptyString
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Calendar

@RunWith(JUnit4::class)
class EditViewInitialStateTest {
    private val initialState = EditViewState.initialState()

    @Test
    fun checkTitleState() {
        assertEquals(emptyString(), initialState.initialTitle)
        assertEquals(emptyString(), initialState.currentTitle)
        assertNull(initialState.titleErrorMsg)
        assertFalse(initialState.isTitleChanged())
    }

    @Test
    fun checkColorPickerState() {
        assertEquals(ProgressColor.COLOR_GRAY, initialState.progressColor)
        assertTrue(initialState.lockColorPicker)
    }

    @Test
    fun checkNotificationState() {
        assertTrue(initialState.lockNotification)
        assertTrue(initialState.notificationList.isEmpty())
    }

    @Test
    fun checkStartDate() {
        val nowCal = Calendar.getInstance()

        val startCal = Calendar.getInstance().apply { time = initialState.progressStartTime }
        assertEquals(nowCal.get(Calendar.DAY_OF_MONTH), startCal.get(Calendar.DAY_OF_MONTH))
        assertEquals(nowCal.get(Calendar.MONTH), startCal.get(Calendar.MONTH))
        assertEquals(nowCal.get(Calendar.YEAR), startCal.get(Calendar.YEAR))
        assertEquals(0, startCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, startCal.get(Calendar.MINUTE))
        assertEquals(0, startCal.get(Calendar.SECOND))
        assertEquals(0, startCal.get(Calendar.MILLISECOND))
    }

    @Test
    fun checkEndDate() {
        val nowCal = Calendar.getInstance()

        val endCal = Calendar.getInstance().apply { time = initialState.progressEndTime }
        assertEquals(nowCal.get(Calendar.DAY_OF_MONTH) + 1, endCal.get(Calendar.DAY_OF_MONTH))
        assertEquals(nowCal.get(Calendar.MONTH), endCal.get(Calendar.MONTH))
        assertEquals(nowCal.get(Calendar.YEAR), endCal.get(Calendar.YEAR))
        assertEquals(23, endCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(59, endCal.get(Calendar.MINUTE))
        assertEquals(59, endCal.get(Calendar.SECOND))
        assertEquals(999, endCal.get(Calendar.MILLISECOND))
    }

    @Test
    fun checkProgressTypeState() {
        assertEquals(ProgressType.CUSTOM, initialState.progressType)
    }

    @Test
    fun checkOtherInitialState() {
        assertFalse(initialState.isLoadingProgress)
        assertFalse(initialState.isSomethingChanged)
        assertTrue(initialState.allowEditDate)
    }
}
