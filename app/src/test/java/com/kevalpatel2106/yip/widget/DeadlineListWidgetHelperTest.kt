package com.kevalpatel2106.yip.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.kevalpatel2106.testutils.getKFixture
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigTheme
import com.kevalpatel2106.yip.generateDeadline
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(Enclosed::class)
class DeadlineListWidgetHelperTest {

    @RunWith(JUnit4::class)
    class JunitTest {

        @Mock
        lateinit var context: Context

        private val deadLine = generateDeadline(getKFixture())

        @Before
        fun before() {
            MockitoAnnotations.initMocks(this)
            whenever(context.getString(any(), any()))
                .thenReturn("%.2f%% percent")
            whenever(context.getString(any(), any(), any(), any()))
                .thenReturn("%d %d %d time left")
        }

        @Test
        fun `given widget theme light when get background drawable check light background returned`() {
            // given
            val theme = WidgetConfigTheme.LIGHT

            // when
            val background = DeadlineListWidgetHelper.getWidgetBackground(theme)

            // check
            assertEquals(R.drawable.bg_widget_white, background)
        }

        @Test
        fun `given widget theme dark when get background drawable check dark background returned`() {
            // given
            val theme = WidgetConfigTheme.DARK

            // when
            val background = DeadlineListWidgetHelper.getWidgetBackground(theme)

            // check
            assertEquals(R.drawable.bg_widget_dark, background)
        }

        @Test
        fun `given widget theme light when get title color check accent color returned`() {
            // given
            val theme = WidgetConfigTheme.LIGHT

            // when
            val textColor = DeadlineListWidgetHelper.getTitleColor(theme)

            // check
            assertEquals(R.color.colorAccent, textColor)
        }

        @Test
        fun `given widget theme light when get title color check white color returned`() {
            // given
            val theme = WidgetConfigTheme.DARK

            // when
            val textColor = DeadlineListWidgetHelper.getTitleColor(theme)

            // check
            assertEquals(android.R.color.white, textColor)
        }

        @Test
        fun `given widget theme light when get list row text color check black color returned`() {
            // given
            val theme = WidgetConfigTheme.LIGHT

            // when
            val textColor = DeadlineListWidgetHelper.getListRowTextColor(theme)

            // check
            assertEquals(android.R.color.black, textColor)
        }

        @Test
        fun `given widget theme dark when get list row text color check white color returned`() {
            // given
            val theme = WidgetConfigTheme.DARK

            // when
            val textColor = DeadlineListWidgetHelper.getTitleColor(theme)

            // check
            assertEquals(android.R.color.white, textColor)
        }

        @Test
        fun `given widget content percent when get list row text check percent text returned`() {
            // given
            val content = WidgetConfigContent.PERCENT

            // when
            val text = DeadlineListWidgetHelper.getContent(context, content, deadLine)

            // check
            assertTrue(text.contains("percent"))
        }

        @Test
        fun `given widget content time left when get list text check time left text returned`() {
            // given
            val content = WidgetConfigContent.TIME_LEFT

            // when
            val text = DeadlineListWidgetHelper.getContent(context, content, deadLine)

            // check
            assertTrue(text.contains("time left"))
        }
    }

    @RunWith(RobolectricTestRunner::class)
    class RobolectricTests {
        private val kFixture = getKFixture()

        @Test
        fun `given widget id check widget intent`() {
            // given
            val widgetId = kFixture<Int>()

            // when
            val intent = DeadlineListWidgetHelper.getWidgetServiceIntent(
                ApplicationProvider.getApplicationContext(),
                widgetId
            )

            // check
            assertEquals(widgetId, intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1))
        }
    }
}
