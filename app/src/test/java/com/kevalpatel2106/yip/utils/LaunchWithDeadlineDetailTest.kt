package com.kevalpatel2106.yip.utils

import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class LaunchWithDeadlineDetailTest {
    private val testDeadlineId = 324L
    private lateinit var intent: Intent

    @Before
    fun before() {
        intent = AppLaunchHelper.launchWithDeadlineDetail(
            context = RuntimeEnvironment.application,
            idToLaunch = testDeadlineId
        )
    }

    @Test
    fun checkIntentArguments() {
        assertEquals(testDeadlineId, intent.getLongExtra(AppLaunchHelper.ARG_DEADLINE_ID, -1))
    }

    @Test
    fun checkIntentAction() {
        assertEquals(AppLaunchHelper.ACTION_LAUNCH_WITH_DEADLINE, intent.action)
    }

    @Test
    fun checkIntentFlags() {
        assertEquals(
            Intent.FLAG_ACTIVITY_CLEAR_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK,
            intent.flags
        )
    }
}
