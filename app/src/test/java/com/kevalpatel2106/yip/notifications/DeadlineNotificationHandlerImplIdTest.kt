package com.kevalpatel2106.yip.notifications

import android.content.Context
import com.kevalpatel2106.yip.utils.AppLaunchIntentProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(Parameterized::class)
class DeadlineNotificationHandlerImplIdTest(
    private val deadlineId: Long,
    private val notificationId: Int
) {

    @Mock
    lateinit var context: Context

    @Mock
    internal lateinit var appLaunchIntentProvider: AppLaunchIntentProvider

    private lateinit var deadlineNotificationHandler: DeadlineNotificationHandlerImpl

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)

        deadlineNotificationHandler =
            DeadlineNotificationHandlerImpl(context, appLaunchIntentProvider)
    }

    @Test
    fun `when generate notification id called check notification id`() {
        // when
        val id = deadlineNotificationHandler.generateNotificationId(deadlineId)

        // check
        assertEquals(notificationId, id)
    }

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
}
