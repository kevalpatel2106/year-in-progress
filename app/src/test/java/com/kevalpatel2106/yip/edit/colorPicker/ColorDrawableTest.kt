package com.kevalpatel2106.yip.edit.colorPicker

import android.content.Context
import android.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ColorDrawableTest {

    @Mock
    lateinit var context: Context

    private lateinit var colorDrawable: ColorDrawable

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        colorDrawable = ColorDrawable(context)
    }

    @Test
    fun checkDefaultIsLockStatus() {
        assertFalse(colorDrawable.isLocked)
    }

    @Test
    fun checkDefaultCircleRadius() {
        assertEquals(ColorDrawable.DEFAULT_CORNER_RADIUS, colorDrawable.circleRadius)
    }

    @Test
    fun checkDefaultCircleStrokeWidth() {
        assertEquals(ColorDrawable.DEFAULT_STROKE_WIDTH, colorDrawable.circleStrokeWidth)
    }

    @Test
    fun checkDefaultCircleColor() {
        assertEquals(Color.WHITE, colorDrawable.circleColor)
    }
}
