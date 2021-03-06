package com.kevalpatel2106.yip.core.ext

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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

@RunWith(Enclosed::class)
class ViewExtTests {

    @RunWith(Parameterized::class)
    class ActionbarSetUpTests(
        private val isTitleEnabled: Boolean,
        private val isHomeEnabled: Boolean
    ) {

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

            Mockito.verify(mockActionBar, Mockito.times(1))
                .setDisplayShowTitleEnabled(isTitleVisibleCaptor.capture())
            assertEquals(isTitleEnabled, isTitleVisibleCaptor.value)

            Mockito.verify(mockActionBar, Mockito.times(1))
                .setDisplayShowHomeEnabled(homeEnabledCaptor.capture())
            assertEquals(isHomeEnabled, homeEnabledCaptor.value)

            Mockito.verify(mockActionBar, Mockito.times(1))
                .setDisplayHomeAsUpEnabled(homeAsUpEnabledCaptor.capture())
            assertEquals(isHomeEnabled, homeAsUpEnabledCaptor.value)
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
            val view = View(ApplicationProvider.getApplicationContext<Context>())
            view.showOrHide(true)
            assertTrue(view.isVisible)
        }


        @Test
        @Throws(Exception::class)
        fun testViewHide() {
            val view = View(ApplicationProvider.getApplicationContext<Context>())
            view.showOrHide(false)
            assertTrue(!view.isVisible)
        }

        @Test
        @Throws(Exception::class)
        fun testMenuItemShow() {
            mockMenuItem.showOrHideLoader(
                ApplicationProvider.getApplicationContext<Context>(),
                true
            )

            Mockito.verify(mockMenuItem, Mockito.times(1)).isEnabled = isEnabledCaptor.capture()
            assertFalse(isEnabledCaptor.value)

            Mockito.verify(mockMenuItem, Mockito.times(1)).actionView = actionViewCaptor.capture()
            assertNotNull(actionViewCaptor.value)
            assertTrue(actionViewCaptor.value is ProgressBar)
        }

        @Test
        @Throws(Exception::class)
        fun testMenuItemHide() {
            mockMenuItem.showOrHideLoader(
                ApplicationProvider.getApplicationContext<Context>(),
                false
            )

            Mockito.verify(mockMenuItem, Mockito.times(1)).isEnabled = isEnabledCaptor.capture()
            assertTrue(isEnabledCaptor.value)

            Mockito.verify(mockMenuItem, Mockito.times(1)).actionView = actionViewCaptor.capture()
            assertNull(actionViewCaptor.value)
        }
    }
}
