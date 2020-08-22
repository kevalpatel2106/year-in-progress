package com.kevalpatel2106.yip.widget.config

import androidx.annotation.DrawableRes
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigTheme
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class WidgetConfigUseCaseTest(
    private val theme: WidgetConfigTheme,
    private val content: WidgetConfigContent,
    @DrawableRes private val preview: Int
) {

    @Test
    fun `given them and content when get preview image check preview image res`() {
        assertEquals(preview, WidgetConfigUseCase.getPreviewImage(content, theme))
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(
                    WidgetConfigTheme.LIGHT,
                    WidgetConfigContent.PERCENT,
                    R.drawable.list_widget_preview_percent_light
                ),
                arrayOf(
                    WidgetConfigTheme.LIGHT,
                    WidgetConfigContent.TIME_LEFT,
                    R.drawable.list_widget_preview_time_left_light
                ),
                arrayOf(
                    WidgetConfigTheme.LIGHT,
                    WidgetConfigContent.PERCENT,
                    R.drawable.list_widget_preview_percent_light
                ),
                arrayOf(
                    WidgetConfigTheme.DARK,
                    WidgetConfigContent.TIME_LEFT,
                    R.drawable.list_widget_preview_time_left_dark
                )
            )
        }
    }
}
