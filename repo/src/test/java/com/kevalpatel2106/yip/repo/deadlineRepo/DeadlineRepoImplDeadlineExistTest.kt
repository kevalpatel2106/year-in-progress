package com.kevalpatel2106.yip.repo.deadlineRepo

import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyLong

@RunWith(JUnit4::class)
internal class DeadlineRepoImplDeadlineExistTest : DeadlineRepoImplTestSetUp() {

    @Test
    fun `given deadline count more than 0 when checking is deadline exist check result is true`() {
        // given
        whenever(deadlineDao.getCount(anyLong())).thenReturn(Single.just(1))

        // when
        val testSubscriber = deadlineRepo.isDeadlineExist(kFixture()).test()
        testSubscriber.awaitTerminalEvent()

        // then
        assertDeadlineExist(testSubscriber, true)
    }

    @Test
    fun `given deadline count is 0 when checking is deadline exist check result is false`() {
        // given
        whenever(deadlineDao.getCount(anyLong())).thenReturn(Single.just(0))

        // when
        val testSubscriber = deadlineRepo.isDeadlineExist(kFixture()).test()
        testSubscriber.awaitTerminalEvent()

        // then
        assertDeadlineExist(testSubscriber, false)
    }

    @Test
    fun `given deadline id when observing deadline error occurs check error message`() {
        // given
        val throwable = Throwable(kFixture<String>())
        whenever(deadlineDao.getCount(anyLong())).thenReturn(Single.error(throwable))

        // when
        val testSubscriber = deadlineRepo.isDeadlineExist(kFixture()).test()
        testSubscriber.awaitTerminalEvent()

        // then
        testSubscriber.assertNoTimeout()
            .assertError { it.message == throwable.message }
            .assertTerminated()
    }

    private fun assertDeadlineExist(testSubscriber: TestObserver<Boolean>, exist: Boolean) {
        testSubscriber.assertNoTimeout()
            .assertNoErrors()
            .assertTerminated()
            .assertValueCount(1)
            .assertValueAt(0) { it == exist }
    }
}
