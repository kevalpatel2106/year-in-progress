package com.kevalpatel2106.yip.core.ext


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ContextExtensionKtTest {

    @Test
    fun `given color res when get color compat called check color int`() {
        // given
        val colorRes = android.R.color.white

        // when
        val colorInt = ApplicationProvider.getApplicationContext<Context>()
            .getColorCompat(colorRes)

        // check
        assertEquals(Color.WHITE, colorInt)
    }

    @Test
    fun `given is new task when get launch intent called check intent flags`() {
        // given
        val isNewTask = true

        // when
        val launchIntent = ApplicationProvider.getApplicationContext<Context>()
            .getLaunchIntent(TestActivity::class.java, isNewTask)

        // check
        assertEquals(TestActivity::class.java.name, launchIntent.component!!.className)
        assertEquals(
            Intent.FLAG_ACTIVITY_CLEAR_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK, launchIntent.flags
        )
    }

    @Test
    fun `given is new task false when get launch intent called check intent flags`() {
        // given
        val isNewTask = false

        // when
        val launchIntent = ApplicationProvider.getApplicationContext<Context>()
            .getLaunchIntent(TestActivity::class.java, isNewTask)

        // check
        assertEquals(TestActivity::class.java.name, launchIntent.component!!.className)
        assertEquals(0, launchIntent.flags)
    }

    @Test
    @Throws(Exception::class)
    fun `given darken factor when darken color called check output color`() {
        // given
        val factor = 0.5f
        val originalColorArray = FloatArray(3)
        Color.colorToHSV(Color.DKGRAY, originalColorArray)

        // when
        val darkerColorArray = FloatArray(3)
        Color.colorToHSV(darkenColor(Color.DKGRAY, factor), darkerColorArray)

        // check
        assertEquals(darkerColorArray[2], originalColorArray[2] * factor)
    }

    @Test
    @Throws(Exception::class)
    fun `given test color when get background gradient check gradient orientation`() {
        // given
        val testColor = Color.BLACK

        // when
        val gradientDrawable = ApplicationProvider.getApplicationContext<Context>()
            .getBackgroundGradient(testColor)

        // Check
        assertEquals(GradientDrawable.Orientation.LEFT_RIGHT, gradientDrawable.orientation)
    }

    @Test
    fun `given test color when get background gradient check gradient colors`() {
        // given
        val testColor = Color.BLACK

        // when
        val gradientDrawable = ApplicationProvider.getApplicationContext<Context>()
            .getBackgroundGradient(testColor)

        // Check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            assertEquals(6, gradientDrawable.colors?.size)
            assertEquals(darkenColor(testColor, 0.7f), gradientDrawable.colors!![0])
            assertEquals(darkenColor(testColor, 0.8f), gradientDrawable.colors!![1])
            assertEquals(darkenColor(testColor, 0.85f), gradientDrawable.colors!![2])
            assertEquals(darkenColor(testColor, 0.9f), gradientDrawable.colors!![3])
            assertEquals(darkenColor(testColor, 0.9f), gradientDrawable.colors!![4])
            assertEquals(testColor, gradientDrawable.colors!![5])
        } else {
            assertTrue(true)
        }
    }

    class TestActivity : AppCompatActivity()
}
