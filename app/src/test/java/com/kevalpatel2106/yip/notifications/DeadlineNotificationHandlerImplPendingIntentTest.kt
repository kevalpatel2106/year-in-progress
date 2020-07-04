package com.kevalpatel2106.yip.notifications

import android.content.Context
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.utils.AppLaunchHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DeadlineNotificationHandlerImplPendingIntentTest {
    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private val notificationId = kFixture<Int>()
    private val deadlineId = kFixture<Long>()

    @Mock
    lateinit var context: Context

    @Mock
    internal lateinit var appLaunchHelper: AppLaunchHelper

    private lateinit var deadlineNotificationHandler: DeadlineNotificationHandlerImpl

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)

        deadlineNotificationHandler = DeadlineNotificationHandlerImpl(context, appLaunchHelper)
    }

    @Test
    fun `when generate pending intent called check correct launch intent set`() {
        // when
        deadlineNotificationHandler.getPendingIntent(context, notificationId, deadlineId)

        // check
        Mockito.verify(appLaunchHelper).getLaunchIntentWithDeadlineDetail(context, deadlineId)
    }
}
