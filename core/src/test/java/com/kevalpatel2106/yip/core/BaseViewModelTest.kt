package com.kevalpatel2106.yip.core

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Observable
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class BaseViewModelTest {
    private lateinit var testViewModel: TestViewModel

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        testViewModel = TestViewModel()
    }

    @After
    fun clear() {
        testViewModel.clear()
    }

    @Test
    @Throws(IOException::class)
    fun checkInitOfCompositeDisposable() {
        Assert.assertNotNull(testViewModel.getDisposable())
        Assert.assertEquals(testViewModel.getDisposable().size(), 0)
    }

    @Test
    @Throws(IOException::class)
    fun checkOnClear() {
        testViewModel.getDisposable().add(Observable.timer(10, TimeUnit.SECONDS).subscribe())
        testViewModel.clear()

        Assert.assertEquals(testViewModel.getDisposable().size(), 0)
    }

    @Test
    @Throws(IOException::class)
    fun checkAddDisposable() {
        Assert.assertEquals(testViewModel.getDisposable().size(), 0)

        testViewModel.getDisposable().add(Observable.timer(10, TimeUnit.SECONDS).subscribe())
        Assert.assertEquals(testViewModel.getDisposable().size(), 1)
    }
}

private class TestViewModel : BaseViewModel() {

    fun clear() = onCleared()

    fun getDisposable() = compositeDisposable
}
