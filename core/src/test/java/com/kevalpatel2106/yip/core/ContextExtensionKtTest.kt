package com.kevalpatel2106.yip.core


import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * Created by Keval on 02/06/18.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(RobolectricTestRunner::class)
class ContextExtensionKtTest {

    @Test
    @Throws(Exception::class)
    fun testGetColor() {
        assertEquals(
            Color.WHITE,
            RuntimeEnvironment.application.getColorCompat(android.R.color.white)
        )
    }


    @Test
    @Throws(Exception::class)
    fun checkPrepareLaunchIntent_InNewTask() {
        val launchIntent = RuntimeEnvironment.application
            .getLaunchIntent(TestActivity::class.java, true)

        assertEquals(TestActivity::class.java.name, launchIntent.component!!.className)
        assertEquals(
            Intent.FLAG_ACTIVITY_CLEAR_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK, launchIntent.flags
        )
    }

    @Test
    @Throws(Exception::class)
    fun checkPrepareLaunchIntent_WithoutNewTask() {
        val launchIntent = RuntimeEnvironment.application
            .getLaunchIntent(TestActivity::class.java, false)

        assertEquals(TestActivity::class.java.name, launchIntent.component!!.className)
        assertEquals(0, launchIntent.flags)
    }

    class TestActivity : AppCompatActivity()
}
