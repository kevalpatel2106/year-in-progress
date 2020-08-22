package com.kevalpatel2106.yip.entity.ext

import com.kevalpatel2106.yip.entity.WidgetConfigTheme
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class WidgetConfigThemeExtKtTest(
    private val value: String?,
    private val theme: WidgetConfigTheme
) {

    @Test
    fun `given value when getting widget config theme from value check widget thme`() {
        assertEquals(theme, getWidgetConfigTheme(value))
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(WidgetConfigTheme.DARK.value, WidgetConfigTheme.DARK),
                arrayOf(WidgetConfigTheme.LIGHT.value, WidgetConfigTheme.LIGHT),
                arrayOf("abc", WidgetConfigTheme.LIGHT),
                arrayOf(null, WidgetConfigTheme.LIGHT)
            )
        }
    }
}
