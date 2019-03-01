package com.kevalpatel2106.yip.core

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.webkit.WebView
import android.widget.ViewFlipper
import androidx.core.view.isVisible
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.Mockito.times
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class DataBindingAdaptersKtTest {

    private lateinit var view: View

    @Mock
    lateinit var webView: WebView

    @Mock
    lateinit var viewFlipper: ViewFlipper

    @Captor
    lateinit var linkCaptor: ArgumentCaptor<String>

    @Captor
    lateinit var displayChildCaptor: ArgumentCaptor<Int>

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        view = View(RuntimeEnvironment.application)
    }

    @Test
    fun checkVisibility_Show() {
        visibility(view, true)
        Assert.assertTrue(view.isVisible)
    }

    @Test
    fun checkVisibility_Gone() {
        visibility(view, false)
        Assert.assertFalse(view.isVisible)
    }

    @Test
    fun checkBackground() {
        background(view, Color.WHITE)
        Assert.assertEquals(Color.WHITE, (view.background as? ColorDrawable)?.color)
    }

    @Test
    fun checkLoadUrl() {
        val url = "http://google.com"
        loadUrl(webView, url)

        Mockito.verify(webView, times(1)).loadUrl(linkCaptor.capture())
        Assert.assertEquals(url, linkCaptor.value)
    }

    @Test
    fun checkDisplayChild() {
        val pos = 1
        displayChild(viewFlipper, pos)

        Mockito.verify(viewFlipper, times(1)).displayedChild = displayChildCaptor.capture()
        Assert.assertEquals(pos, displayChildCaptor.value)
    }

}