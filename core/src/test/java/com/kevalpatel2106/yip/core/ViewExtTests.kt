package com.kevalpatel2106.yip.core

import android.graphics.Color
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(Enclosed::class)
class ViewExtTests {


    @RunWith(Parameterized::class)
    class ActionbarSetUpTests(private val isTitleEnabled: Boolean, private val isHomeEnabled: Boolean) {

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<Boolean>> {
                @Suppress("BooleanLiteralArgument")
                return arrayListOf(
                        arrayOf(true, true),
                        arrayOf(true, false),
                        arrayOf(false, true),
                        arrayOf(false, false)
                )
            }
        }

        @Captor
        lateinit var isTitleVisibleCaptor: ArgumentCaptor<Boolean>

        @Captor
        lateinit var homeEnabledCaptor: ArgumentCaptor<Boolean>

        @Captor
        lateinit var homeAsUpEnabledCaptor: ArgumentCaptor<Boolean>

        @Mock
        lateinit var mockActionBar: ActionBar

        @Before
        fun init() {
            MockitoAnnotations.initMocks(this@ActionbarSetUpTests)
        }

        @Test
        fun checkActionbarSetUp() {
            mockActionBar.set(isHomeEnabled, isTitleEnabled)

            Mockito.verify(mockActionBar, Mockito.times(1)).setDisplayShowTitleEnabled(isTitleVisibleCaptor.capture())
            org.junit.Assert.assertEquals(isTitleEnabled, isTitleVisibleCaptor.value)

            Mockito.verify(mockActionBar, Mockito.times(1)).setDisplayShowHomeEnabled(homeEnabledCaptor.capture())
            org.junit.Assert.assertEquals(isHomeEnabled, homeEnabledCaptor.value)

            Mockito.verify(mockActionBar, Mockito.times(1)).setDisplayHomeAsUpEnabled(homeAsUpEnabledCaptor.capture())
            org.junit.Assert.assertEquals(isHomeEnabled, homeAsUpEnabledCaptor.value)
        }
    }


    @RunWith(RobolectricTestRunner::class)
    class ViewExtRETest {

        @Captor
        lateinit var isEnabledCaptor: ArgumentCaptor<Boolean>

        @Captor
        lateinit var actionViewCaptor: ArgumentCaptor<View>

        @Mock
        lateinit var mockMenuItem: MenuItem

        @Before
        fun init() {
            MockitoAnnotations.initMocks(this@ViewExtRETest)
        }

        @Test
        @Throws(Exception::class)
        fun testViewShow() {
            val view = View(RuntimeEnvironment.application)
            view.showOrHide(true)
            Assert.assertTrue(view.isVisible)
        }


        @Test
        @Throws(Exception::class)
        fun testViewHide() {
            val view = View(RuntimeEnvironment.application)
            view.showOrHide(false)
            Assert.assertTrue(!view.isVisible)
        }

        @Test
        @Throws(Exception::class)
        fun testDarkerColor() {
            val factor = 0.5f

            val originalColorArray = FloatArray(3)
            Color.colorToHSV(Color.DKGRAY, originalColorArray)

            val darkerColorArray = FloatArray(3)
            Color.colorToHSV(darkenColor(Color.DKGRAY, factor), darkerColorArray)

            Assert.assertEquals(darkerColorArray[2], originalColorArray[2] * factor)
        }

        @Test
        @Throws(Exception::class)
        fun testMenuItemShow() {
            mockMenuItem.showOrHideLoader(RuntimeEnvironment.application, true)

            Mockito.verify(mockMenuItem, Mockito.times(1)).isEnabled = isEnabledCaptor.capture()
            Assert.assertFalse(isEnabledCaptor.value)

            Mockito.verify(mockMenuItem, Mockito.times(1)).actionView = actionViewCaptor.capture()
            Assert.assertNotNull(actionViewCaptor.value)
            Assert.assertTrue(actionViewCaptor.value is ProgressBar)
        }

        @Test
        @Throws(Exception::class)
        fun testMenuItemHide() {
            mockMenuItem.showOrHideLoader(RuntimeEnvironment.application, false)

            Mockito.verify(mockMenuItem, Mockito.times(1)).isEnabled = isEnabledCaptor.capture()
            Assert.assertTrue(isEnabledCaptor.value)

            Mockito.verify(mockMenuItem, Mockito.times(1)).actionView = actionViewCaptor.capture()
            Assert.assertNull(actionViewCaptor.value)
        }
    }
}