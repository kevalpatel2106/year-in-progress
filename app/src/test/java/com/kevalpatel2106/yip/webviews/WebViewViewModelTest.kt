package com.kevalpatel2106.yip.webviews

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.testutils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class WebViewViewModelTest {
    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }

    private val testTitleRes = kFixture.range(1..1000)
    private val testTitle = kFixture<String>()
    private val testLink = kFixture<String>()

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    internal lateinit var application: Application

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this@WebViewViewModelTest)
        whenever(application.getString(testTitleRes)).thenReturn(testTitle)
    }

    @Test
    fun `when view model initialised check initial view state`() {
        // when
        val viewModel = WebViewViewModel(application)

        // check
        assertEquals(WebViewViewState.initialState(), viewModel.viewState.getOrAwaitValue())
    }

    @Test
    fun `when submitting invalid title check exception`() {
        // given
        val viewModel = WebViewViewModel(application)
        val invalidTitle = kFixture.range(-1000..0)

        // when - check
        try {
            viewModel.submitLink(testLink, invalidTitle)
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals("Invalid link: $testLink or title: $invalidTitle", e.message)
        }
    }

    @Test
    fun `when submitting invalid link check exception`() {
        // given
        val viewModel = WebViewViewModel(application)

        // when - check
        try {
            viewModel.submitLink(null, testTitleRes)
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals("Invalid link: null or title: $testTitleRes", e.message)
        }
    }

    @Test
    fun `given link and title valid when submitted check flipper position`() {
        // given
        val viewModel = WebViewViewModel(application)

        // when
        viewModel.submitLink(testLink, testTitleRes)

        // check
        assertEquals(
            WebviewFlipperPosition.POS_LOADING,
            viewModel.flipperPosition.getOrAwaitValue()
        )
    }

    @Test
    fun `given link and title valid when submitted check link in view state`() {
        // given
        val viewModel = WebViewViewModel(application)

        // when
        viewModel.submitLink(testLink, testTitleRes)

        // check
        assertEquals(testLink, viewModel.viewState.getOrAwaitValue().linkUrl)
    }


    @Test
    fun `given link and title valid when submitted check title in view state`() {
        // given
        val viewModel = WebViewViewModel(application)

        // when
        viewModel.submitLink(testLink, testTitleRes)

        // check
        assertEquals(testTitle, viewModel.viewState.getOrAwaitValue().title)
    }

    @Test
    fun `given link is submitted when page loaded check flipper position`() {
        // given
        val viewModel = WebViewViewModel(application)
        viewModel.submitLink(testLink, testTitleRes)

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
        val viewModel = WebViewViewModel(application)
        viewModel.submitLink(testLink, testTitleRes)

        // when
        viewModel.onPageLoadingFailed()

        // check
        assertEquals(WebviewFlipperPosition.POS_ERROR, viewModel.flipperPosition.getOrAwaitValue())
    }

    @Test
    fun `given link is submitted when page reloaded check flipper position`() {
        // given
        val viewModel = WebViewViewModel(application)
        viewModel.submitLink(testLink, testTitleRes)

        // when
        viewModel.reload()

        // check
        assertEquals(
            WebviewFlipperPosition.POS_LOADING,
            viewModel.flipperPosition.getOrAwaitValue()
        )
    }
}
