package com.kevalpatel2106.yip.core

import android.view.View
import android.webkit.WebView
import android.widget.ViewFlipper
import androidx.core.view.isVisible
import com.kevalpatel2106.yip.core.ext.emptyString
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(Enclosed::class)
class DataBindingAdaptersKtTest {

    @RunWith(RobolectricTestRunner::class)
    class ViewDataBindingTest {

        private lateinit var view: View

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

    }

    @RunWith(RobolectricTestRunner::class)
    class WebViewDataBindingTest {

        @Mock
        lateinit var webView: WebView

        @Captor
        lateinit var linkCaptor: ArgumentCaptor<String>

        @Before
        fun before() {
            MockitoAnnotations.initMocks(this)
        }

        @Test
        fun checkLoadEmptyUrl_withWebView() {
            val url = emptyString()
            loadUrl(webView, url)
            Mockito.verify(webView, never()).loadUrl(ArgumentMatchers.anyString())
        }

        @Test
        fun checkLoadUrl_withWebView() {
            val url = "http://google.com"
            loadUrl(webView, url)

            Mockito.verify(webView, times(1)).loadUrl(linkCaptor.capture())
            Assert.assertEquals(url, linkCaptor.value)
        }

        @Test
        fun checkLoadUrl_withView() {
            val url = "http://google.com"
            loadUrl(View(RuntimeEnvironment.application), url)
            Mockito.verify(webView, never()).loadUrl(url)
        }

    }

    @RunWith(RobolectricTestRunner::class)
    class ViewFlipperDataBindingTest {

        @Mock
        lateinit var viewFlipper: ViewFlipper

        @Captor
        lateinit var displayChildCaptor: ArgumentCaptor<Int>

        @Before
        fun before() {
            MockitoAnnotations.initMocks(this)
        }

        @Test
        fun checkDisplayChild_witViewFlipper() {
            val pos = 1
            displayChild(viewFlipper, pos)

            Mockito.verify(viewFlipper, times(1)).displayedChild = displayChildCaptor.capture()
            Assert.assertEquals(pos, displayChildCaptor.value)
        }

        @Test
        fun checkDisplayChild_witView() {
            val pos = 1
            displayChild(View(RuntimeEnvironment.application), pos)
            Mockito.verify(viewFlipper, never()).displayedChild = pos
        }
    }
}
