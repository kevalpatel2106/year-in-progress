package com.kevalpatel2106.yip.notifications

import android.content.Context
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.utils.AppLaunchIntentProvider
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.MockitoAnnotations


@RunWith(Parameterized::class)
class DeadlineNotificationHandlerImplTitleAndMessageTest(private val percent: Float) {

    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private val deadlineTitle = kFixture<String>()
    private val notificationTitle = "%s reached to %.2f%%"
    private val deadlinePercentageString = "%.2f%%"

    @Mock
    lateinit var context: Context

    @Mock
    internal lateinit var appLaunchIntentProvider: AppLaunchIntentProvider

    private lateinit var deadlineNotificationHandler: DeadlineNotificationHandlerImpl

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        whenever(context.getString(R.string.deadline_notification_title))
            .thenReturn(notificationTitle)
        whenever(context.getString(R.string.deadline_percentage))
            .thenReturn(deadlinePercentageString)

        deadlineNotificationHandler =
            DeadlineNotificationHandlerImpl(context, appLaunchIntentProvider)
    }

    @Test
    fun `given deadline title when get notification title called check the notification title `() {
        // when
        val title = deadlineNotificationHandler.getTitle(context, deadlineTitle, percent)

        // check
        assertEquals(String.format(notificationTitle, deadlineTitle, percent), title)
    }

    @Test
    fun `given deadline title when get notification message called check the notification message `() {
        // when
        val message = deadlineNotificationHandler.getMessage(context, percent)

        // check
        assertEquals(
            percent.toInt() / 2,
            message.count { it.toString() == DeadlineNotificationHandlerImpl.COMPLETE_DOT })
        assertEquals(
            (100 - percent.toInt()) / 2,
            message.count { it.toString() == DeadlineNotificationHandlerImpl.INCOMPLETE_DOT })
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Float> {
            return arrayListOf(0f, 10.55f, 50.99f, 66.66f, 100.0f)
        }
    }
}
