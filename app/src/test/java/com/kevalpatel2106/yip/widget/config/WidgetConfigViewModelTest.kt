package com.kevalpatel2106.yip.widget.config

import android.app.Activity
import android.appwidget.AppWidgetManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.flextrade.kfixture.KFixture
import com.kevalpatel2106.testutils.getKFixture
import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.WidgetConfig
import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigTheme
import com.kevalpatel2106.yip.repo.widgetConfig.WidgetConfigRepo
import com.kevalpatel2106.yip.widget.config.WidgetConfigSingleEvent.CloseScreen
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(Enclosed::class)
class WidgetConfigViewModelTest {

    class JunitTest {
        @Rule
        @JvmField
        val rule: TestRule = InstantTaskExecutorRule()

        @Mock
        lateinit var repo: WidgetConfigRepo

        private val kFixture: KFixture = getKFixture()
        private val sampleWidgetConfig = WidgetConfig(
            id = kFixture(),
            theme = WidgetConfigTheme.LIGHT,
            content = WidgetConfigContent.PERCENT
        )
        private lateinit var model: WidgetConfigViewModel

        @Before
        fun before() {
            MockitoAnnotations.initMocks(this)
            model = WidgetConfigViewModel(repo)

            whenever(repo.getWidgetConfig(sampleWidgetConfig.id)).thenReturn(sampleWidgetConfig)
        }

        @Test
        fun `given widget id not set yet check initial view state`() {
            // check
            assertEquals(WidgetConfigViewState.initialState(), model.viewState.getOrAwaitValue())
        }

        @Test
        fun `given widget id is invalid when save widget id then close screen with result cancle`() {
            // given
            val widgetId = AppWidgetManager.INVALID_APPWIDGET_ID

            // then
            model.setWidgetId(widgetId)

            // check
            assertEquals(
                CloseScreen(Activity.RESULT_CANCELED, widgetId),
                model.singleEvent.getOrAwaitValue()
            )
        }

        @Test
        fun `given widget id is valid when save widget id then check view state`() {
            // then
            model.setWidgetId(sampleWidgetConfig.id)

            // check
            assertEquals(sampleWidgetConfig.id, model.viewState.getOrAwaitValue().widgetId)
            assertEquals(
                R.id.widget_config_light_theme_radio,
                model.viewState.getOrAwaitValue().selectedThemeId
            )
            assertEquals(
                R.id.widget_config_percent_content_radio,
                model.viewState.getOrAwaitValue().selectedContentId
            )
            assertEquals(
                R.drawable.list_widget_preview_percent_light,
                model.viewState.getOrAwaitValue().previewImageRes
            )
            assertTrue(model.viewState.getOrAwaitValue().applyButtonEnabled)
        }

        @Test
        fun `given widget id set when apply widget config check apply button disabled`() {
            // given
            model.setWidgetId(sampleWidgetConfig.id)

            // when
            model.onApplyWidgetConfig()

            // check
            assertFalse(model.viewState.getOrAwaitValue().applyButtonEnabled)
        }

        @Test
        fun `given widget id set when apply widget config check screen closed`() {
            // given
            model.setWidgetId(sampleWidgetConfig.id)

            // when
            model.onApplyWidgetConfig()

            // check
            assertEquals(
                CloseScreen(Activity.RESULT_OK, sampleWidgetConfig.id),
                model.singleEvent.getOrAwaitValue()
            )
        }

        @Test
        fun `given widget id set when apply widget config check config saved to preference`() {
            // given
            model.setWidgetId(sampleWidgetConfig.id)

            // when
            model.onApplyWidgetConfig()

            // check
            val widgetIdCaptor = argumentCaptor<Int>()
            val widgetContentCaptor = argumentCaptor<WidgetConfigContent>()
            val widgetThemeCaptor = argumentCaptor<WidgetConfigTheme>()
            verify(repo).saveWidgetConfig(
                widgetId = widgetIdCaptor.capture(),
                content = widgetContentCaptor.capture(),
                theme = widgetThemeCaptor.capture()
            )
            assertEquals(sampleWidgetConfig.id, widgetIdCaptor.lastValue)
            assertEquals(sampleWidgetConfig.content, widgetContentCaptor.lastValue)
            assertEquals(sampleWidgetConfig.theme, widgetThemeCaptor.lastValue)
        }
    }

    @RunWith(Parameterized::class)
    class WidgetConfigChangedTest(
        private val selectedThemeRadioId: Int,
        private val selectedContentRadioId: Int,
        private val shouldViewStateUpdate: Boolean
    ) {
        @Rule
        @JvmField
        val rule: TestRule = InstantTaskExecutorRule()

        @Mock
        lateinit var repo: WidgetConfigRepo

        @Mock
        lateinit var observer: Observer<WidgetConfigViewState>

        private val kFixture: KFixture = getKFixture()
        private val storedWidgetConfig = WidgetConfig(
            id = kFixture(),
            theme = WidgetConfigTheme.LIGHT,
            content = WidgetConfigContent.PERCENT
        )
        private lateinit var model: WidgetConfigViewModel

        @Before
        fun before() {
            MockitoAnnotations.initMocks(this)
            whenever(repo.getWidgetConfig(storedWidgetConfig.id)).thenReturn(storedWidgetConfig)

            model = WidgetConfigViewModel(repo)
            model.setWidgetId(storedWidgetConfig.id)

            model.viewState.observeForever(observer)
        }

        @After
        fun after() {
            model.viewState.removeObserver(observer)
        }

        @Test
        fun `given selected radio and them id when config changed check view state updates or not`() {
            // when
            model.onWidgetConfigChanged(selectedThemeRadioId, selectedContentRadioId)

            // then
            verify(observer, times(1 + (if (shouldViewStateUpdate) 1 else 0))).onChanged(any())
        }


        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any?>> {
                return arrayListOf(
                    arrayOf(-1, R.id.widget_config_time_left_content_radio, false),
                    arrayOf(R.id.widget_config_dark_theme_radio, -1, false),
                    arrayOf(
                        R.id.widget_config_dark_theme_radio,
                        R.id.widget_config_percent_content_radio,
                        true
                    ),
                    arrayOf(
                        R.id.widget_config_light_theme_radio,
                        R.id.widget_config_time_left_content_radio,
                        true
                    ),
                    arrayOf(
                        R.id.widget_config_light_theme_radio,
                        R.id.widget_config_percent_content_radio,
                        false
                    )
                )
            }
        }
    }
}
