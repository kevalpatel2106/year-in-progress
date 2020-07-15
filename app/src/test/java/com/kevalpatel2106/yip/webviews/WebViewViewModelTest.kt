package com.kevalpatel2106.yip.webviews

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.testutils.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WebViewViewModelTest {
    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }

    private val testTitle = kFixture<String>()
    private val testLink = kFixture<String>()

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `when view model initialised check initial view state`() {
        // when
        val viewModel = WebViewViewModel()

        // check
        assertEquals(WebViewViewState.initialState(), viewModel.viewState.getOrAwaitValue())
    }

    @Test
    fun `given link and title valid when submitted check link in view state`() {
        // given
        val viewModel = WebViewViewModel()

        // when
        viewModel.submitLink(testLink, testTitle)

        // check
        assertEquals(testLink, viewModel.viewState.getOrAwaitValue().linkUrl)
    }


    @Test
    fun `given link and title valid when submitted check title in view state`() {
        // given
        val viewModel = WebViewViewModel()

        // when
        viewModel.submitLink(testLink, testTitle)

        // check
        assertEquals(testTitle, viewModel.viewState.getOrAwaitValue().title)
    }

    @Test
    fun `given link is submitted when page loaded check flipper position`() {
        // given
        val viewModel = WebViewViewModel()
        viewModel.submitLink(testLink, testTitle)

        // when
        viewModel.onPageLoaded()

        // check
        assertEquals(
            WebviewFlipperPosition.POS_WEBVIEW,
            viewModel.flipperPosition.getOrAwaitValue()
        )
    }

    @Test
    fun `given link is submitted when page loading failed check flipper position`() {
        // given
        val viewModel = WebViewViewModel()
        viewModel.submitLink(testLink, testTitle)

        // when
        viewModel.onPageLoadingFailed()

        // check
        assertEquals(WebviewFlipperPosition.POS_ERROR, viewModel.flipperPosition.getOrAwaitValue())
    }

    @Test
    fun `given link is submitted when page reloaded check flipper position`() {
        // given
        val viewModel = WebViewViewModel()
        viewModel.submitLink(testLink, testTitle)

        // when
        viewModel.reload()

        // check
        assertEquals(
            WebviewFlipperPosition.POS_LOADING,
            viewModel.flipperPosition.getOrAwaitValue()
        )
    }
}
