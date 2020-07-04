package com.kevalpatel2106.yip.notifications

import android.content.Context
import android.os.Build
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class DeadlineNotificationHandlerImplChannelTest {

    @Mock
    lateinit var context: Context

    @Mock
    internal lateinit var appLaunchHelper: AppLaunchHelper

    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private val mockChannelTitle = kFixture<String>()

    private lateinit var deadlineNotificationHandler: DeadlineNotificationHandlerImpl

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        whenever(context.getString(R.string.deadline_notification_channel_title))
            .thenReturn(mockChannelTitle)

        deadlineNotificationHandler = DeadlineNotificationHandlerImpl(context, appLaunchHelper)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun `given api 28 when get notification channel check not null`() {
        // when
        val channel = deadlineNotificationHandler.getDeadlineNotificationChannel(context)

        //check
        assertNotNull(channel)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    fun `given api 21 when get notification channel check null`() {
        // when
        val channel = deadlineNotificationHandler.getDeadlineNotificationChannel(context)

        //check
        assertNull(channel)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun `given api 28 when get notification channel check channel id`() {
        // when
        val channel = deadlineNotificationHandler.getDeadlineNotificationChannel(context)

        //check
        assertEquals(DeadlineNotificationHandlerImpl.CHANNEL_ID, channel?.id)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun `given api 28 when get notification channel check channel name`() {
        // when
        val channel = deadlineNotificationHandler.getDeadlineNotificationChannel(context)

        //check
        assertEquals(mockChannelTitle, channel?.name)
    }
}
