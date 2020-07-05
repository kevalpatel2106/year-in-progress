package com.kevalpatel2106.yip.repo.deadlineRepo

import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyLong
import java.io.IOException

@RunWith(JUnit4::class)
internal class DeadlineRepoImplDeleteDeadlineTest : DeadlineRepoImplTestSetUp() {

    @Test
    fun `given deadline deletes successfully when deleting deadline exist check result`() {
        // given
        doNothing().whenever(deadlineDao).delete(anyLong())

        // when
        val testSubscriber = deadlineRepo.deleteDeadline(kFixture()).test()
        testSubscriber.awaitTerminalEvent()

        // then
        testSubscriber.assertNoTimeout()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun `given deleting deadline generates error when deleting deadline exist check error type`() {
        // given
        val throwable = IOException(kFixture<String>())
        doThrow(throwable).whenever(deadlineDao).delete(anyLong())

        // when
        val testSubscriber = deadlineRepo.deleteDeadline(kFixture()).test()
        testSubscriber.awaitTerminalEvent()

        // then
        testSubscriber.assertNoTimeout()
            .assertTerminated()
            .assertError { it.message == throwable.message }
    }

}
