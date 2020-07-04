package com.kevalpatel2106.yip.webviews

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class WebViewViewModelTest {
    private val testTitle = "test title"
    private val testLink = "test link"

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    internal lateinit var application: Application

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this@WebViewViewModelTest)
        whenever(application.getString(ArgumentMatchers.anyInt())).thenReturn(testTitle)
    }

    @Test
    fun checkInitialViewState() {
        val viewModel = WebViewViewModel(application)
        Assert.assertEquals(WebViewViewState.initialState(), viewModel.viewState.value)
    }

    @Test
    fun checkSubmittingInvalidTitle() {
        val viewModel = WebViewViewModel(application)
        try {
            viewModel.submitLink(testLink, 0)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Invalid link: test link or title: 0", e.message)
        }
    }

    @Test
    fun checkSubmittingInvalidLink() {
        val viewModel = WebViewViewModel(application)
        try {
            viewModel.submitLink(null, 123)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Invalid link: null or title: 123", e.message)
        }
    }

    @Test
    fun checkViewStateAfterPageLoaded() {
        val viewModel = WebViewViewModel(application)
        viewModel.submitLink(testLink, 123)
        viewModel.onPageLoaded()

        Assert.assertEquals(
            WebviewFlipperPosition.POS_WEBVIEW,
            viewModel.flipperPosition.value
        )
        Assert.assertEquals(testLink, viewModel.viewState.value?.linkUrl)
        Assert.assertEquals(testTitle, viewModel.viewState.value?.title)
    }

    @Test
    fun checkViewStateAfterPageLoadingFailed() {
        val viewModel = WebViewViewModel(application)
        viewModel.submitLink(testLink, 123)
        viewModel.onPageLoadingFailed()

        Assert.assertEquals(
            WebviewFlipperPosition.POS_ERROR,
            viewModel.flipperPosition.value
        )
        Assert.assertEquals(testLink, viewModel.viewState.value?.linkUrl)
        Assert.assertEquals(testTitle, viewModel.viewState.value?.title)
    }

    @Test
    fun checkViewStateAfterPageReload() {
        val viewModel = WebViewViewModel(application)
        viewModel.submitLink(testLink, 123)
        viewModel.reload()

        Assert.assertEquals(
            WebviewFlipperPosition.POS_LOADING,
            viewModel.flipperPosition.value
        )
        Assert.assertEquals(testLink, viewModel.viewState.value?.linkUrl)
        Assert.assertEquals(testTitle, viewModel.viewState.value?.title)
    }
}
