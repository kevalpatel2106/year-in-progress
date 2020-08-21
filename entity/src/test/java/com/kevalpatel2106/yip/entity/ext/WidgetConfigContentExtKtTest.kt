package com.kevalpatel2106.yip.entity.ext

import com.kevalpatel2106.yip.entity.WidgetConfigContent
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class WidgetConfigContentExtKtTest(
    private val value: String?,
    private val content: WidgetConfigContent
) {

    @Test
    fun `given value when getting widget config content from value check widget content`() {
        assertEquals(content, getWidgetConfigContent(value))
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(WidgetConfigContent.PERCENT.value, WidgetConfigContent.PERCENT),
                arrayOf(WidgetConfigContent.TIME_LEFT.value, WidgetConfigContent.TIME_LEFT),
                arrayOf("abc", WidgetConfigContent.PERCENT),
                arrayOf(null, WidgetConfigContent.PERCENT)
            )
        }
    }
}
