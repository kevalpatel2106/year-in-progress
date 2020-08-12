package com.kevalpatel2106.yip.core.ext

import android.text.SpannableString
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.robolectric.RobolectricTestRunner

@RunWith(Enclosed::class)
class StringExtKtTest {

    @RunWith(JUnit4::class)
    class StringExtKtTest {
        @Test
        fun checkEmptyString() {
            assertEquals("", emptyString())
        }
    }

    @RunWith(RobolectricTestRunner::class)
    class StringExtKtRETest {
        @Test
        fun checkSpannableString() {
            assertEquals(
                SpannableString(""),
                emptySpannableString()
            )
        }
    }
}
