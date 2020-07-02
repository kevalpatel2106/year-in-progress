package com.kevalpatel2106.yip.notifications

import android.content.Context
import android.os.Build
import com.kevalpatel2106.yip.R
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(Enclosed::class)
class DeadlineNotificationTest {

    @RunWith(Parameterized::class)
    class TitleAndMessageTest(private val percent: Float) {

        companion object {
            private const val DEADLINE_TITLE = "XYZ"
            private const val NOTIFICATION_TITLE = "%s reached to %.2f%%"
            private const val DEADLINE_PERCENTAGE = "%.2f%%"

            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Float> {
                return arrayListOf(0f, 10.55f, 50.99f, 66.66f, 100.0f)
            }
        }

        @Mock
        lateinit var context: Context

        @Before
        fun before() {
            MockitoAnnotations.initMocks(this@TitleAndMessageTest)
            Mockito.`when`(context.getString(R.string.deadline_notification_title))
                .thenReturn(NOTIFICATION_TITLE)
            Mockito.`when`(context.getString(R.string.deadline_percentage))
                .thenReturn(DEADLINE_PERCENTAGE)
        }

        @Test
        fun testNotificationTitle() {
            val title = DeadlineNotification.getTitle(context, DEADLINE_TITLE, percent)
            Assert.assertEquals(String.format(NOTIFICATION_TITLE, DEADLINE_TITLE, percent), title)
        }

        @Test
        fun testNotificationMessage() {
            val message = DeadlineNotification.getMessage(context, percent)
            Assert.assertEquals(
                percent.toInt() / 2,
                message.count { it.toString() == DeadlineNotification.COMPLETE_DOT })
            Assert.assertEquals(
                (100 - percent.toInt()) / 2,
                message.count { it.toString() == DeadlineNotification.INCOMPLETE_DOT })
        }
    }

    @RunWith(Parameterized::class)
    class DeadlineNotificationsIdTest(
        private val deadlineId: Long,
        private val notificationId: Int
    ) {
        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any>> {
                return arrayListOf(
                    arrayOf(0L, 0L.toInt()),
                    arrayOf(28768364L, 28768364L.toInt()),
                    arrayOf(-Long.MAX_VALUE, -Long.MAX_VALUE.toInt()),
                    arrayOf(Long.MAX_VALUE, Long.MAX_VALUE.toInt())
                )
            }
        }

        @Test
        fun checkNotificationId() {
            Assert.assertEquals(
                notificationId,
                DeadlineNotification.generateNotificationId(deadlineId)
            )
        }
    }

    @RunWith(RobolectricTestRunner::class)
    class DeadlineNotificationsChannelTest {
        private val mockChannelTitle = "test channel"

        @Mock
        lateinit var context: Context

        @Before
        fun before() {
            MockitoAnnotations.initMocks(this@DeadlineNotificationsChannelTest)
            Mockito.`when`(context.getString(R.string.deadline_notification_channel_title))
                .thenReturn(mockChannelTitle)
        }

        @Test
        @Config(sdk = [Build.VERSION_CODES.P])
        fun checkNotificationChannelNotNullOnAPI28() {
            Assert.assertNotNull(DeadlineNotification.getDeadlineNotificationChannel(context))
        }

        @Test
        @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
        fun checkNotificationChannelNullOnAPI21() {
            Assert.assertNull(DeadlineNotification.getDeadlineNotificationChannel(context))
        }

        @Test
        @Config(sdk = [Build.VERSION_CODES.P])
        fun testNotificationChannelId() {
            Assert.assertEquals(
                DeadlineNotification.CHANNEL_ID,
                DeadlineNotification.getDeadlineNotificationChannel(context)!!.id
            )
        }

        @Test
        @Config(sdk = [Build.VERSION_CODES.P])
        fun testNotificationChannelName() {
            Assert.assertEquals(
                mockChannelTitle,
                DeadlineNotification.getDeadlineNotificationChannel(context)!!.name
            )
        }
    }
}
