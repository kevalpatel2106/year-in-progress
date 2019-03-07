package com.kevalpatel2106.yip.core

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class SignalLiveDataTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var eventObserver: Observer<Unit>

    private val signalLiveEvent = SignalLiveData()

    @Before
    @Throws(Exception::class)
    fun setUpLifeCycles() {
        MockitoAnnotations.initMocks(this)

        // Start observing
        signalLiveEvent.observeForever(eventObserver)
    }

    @After
    fun after() {
        signalLiveEvent.removeObserver(eventObserver)
    }

    @Test
    fun testInvoke() {
        signalLiveEvent.invoke()
        Mockito.verify(eventObserver, Mockito.times(1)).onChanged(Unit)
    }
}