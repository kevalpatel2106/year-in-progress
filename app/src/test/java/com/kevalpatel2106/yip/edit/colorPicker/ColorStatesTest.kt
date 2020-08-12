package com.kevalpatel2106.yip.edit.colorPicker

import android.graphics.Color
import com.kevalpatel2106.yip.core.ext.emptyString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ColorStatesTest {

    private val colorState = ColorStates(Color.WHITE, false)

    @Test
    fun checkConstructor() {
        assertEquals(Color.WHITE, colorState.color)
        assertEquals(false, colorState.isSelected)
    }

    @Test
    fun checkEquals() {
        val otherColorInt = ColorStates(Color.TRANSPARENT, false)
        val selectedColor = ColorStates(Color.WHITE, true)
        val sameColor = ColorStates(Color.WHITE, false)

        assertEquals(sameColor, colorState)
        assertNotEquals(otherColorInt, colorState)
        assertNotEquals(selectedColor, colorState)
        assertNotEquals(null, colorState)
        assertNotEquals(emptyString(), colorState)
    }

    @Test
    fun checkHashcode() {
        val otherColorInt = ColorStates(Color.TRANSPARENT, false)
        val selectedColor = ColorStates(Color.WHITE, true)
        val sameColor = ColorStates(Color.WHITE, false)

        assertEquals(sameColor.hashCode(), colorState.hashCode())
        assertNotEquals(otherColorInt.hashCode(), colorState.hashCode())
        assertNotEquals(selectedColor.hashCode(), colorState.hashCode())
    }
}
