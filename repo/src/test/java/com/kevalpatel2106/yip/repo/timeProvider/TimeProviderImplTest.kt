package com.kevalpatel2106.yip.repo.timeProvider

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TimeProviderImplTest {

    @Test
    fun testMinuteObserver() {
        val testIntervalMills = 100L

        val testObserver = TimeProviderImpl()
            .minuteObserver(testIntervalMills).test()
        Thread.sleep((4.5 * testIntervalMills).toLong())
        testObserver.dispose()

        testObserver.assertValueCount(5)
            .assertNoErrors()
            .assertValueAt(4) { System.currentTimeMillis() - it.time < 5 * testIntervalMills }
    }

    @Test
    fun testNowAsync() {
        val testObserver = TimeProviderImpl()
            .nowAsync().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNoErrors()
            .assertComplete()
            .assertValue { System.currentTimeMillis() - it.time < 1000L } /*Diff less than 1 sec*/
    }
}
