package com.kevalpatel2106.yip.core

import android.graphics.Color
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.Mockito.times
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class ViewExtKtTest {

    @Captor
    lateinit var isEnabledCaptor: ArgumentCaptor<Boolean>

    @Captor
    lateinit var actionViewCaptor: ArgumentCaptor<View>

    @Mock
    lateinit var mockMenuItem: MenuItem

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this@ViewExtKtTest)
    }

    @Test
    @Throws(Exception::class)
    fun testViewShow() {
        val view = View(RuntimeEnvironment.application)
        view.showOrHide(true)
        assertTrue(view.isVisible)
    }


    @Test
    @Throws(Exception::class)
    fun testViewHide() {
        val view = View(RuntimeEnvironment.application)
        view.showOrHide(false)
        assertTrue(!view.isVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testDarkerColor() {
        val factor = 0.5f

        val originalColorArray = FloatArray(3)
        Color.colorToHSV(Color.DKGRAY, originalColorArray)

        val darkerColorArray = FloatArray(3)
        Color.colorToHSV(darkenColor(Color.DKGRAY, factor), darkerColorArray)

        assertEquals(darkerColorArray[2], originalColorArray[2] * factor)
    }

    @Test
    @Throws(Exception::class)
    fun testMenuItemShow() {
        mockMenuItem.showOrHideLoader(RuntimeEnvironment.application, true)

        Mockito.verify(mockMenuItem, times(1)).isEnabled = isEnabledCaptor.capture()
        assertFalse(isEnabledCaptor.value)

        Mockito.verify(mockMenuItem, times(1)).actionView = actionViewCaptor.capture()
        assertNotNull(actionViewCaptor.value)
        assertTrue(actionViewCaptor.value is ProgressBar)
    }

    @Test
    @Throws(Exception::class)
    fun testMenuItemHide() {
        mockMenuItem.showOrHideLoader(RuntimeEnvironment.application, false)

        Mockito.verify(mockMenuItem, times(1)).isEnabled = isEnabledCaptor.capture()
        assertTrue(isEnabledCaptor.value)

        Mockito.verify(mockMenuItem, times(1)).actionView = actionViewCaptor.capture()
        assertNull(actionViewCaptor.value)
    }
}