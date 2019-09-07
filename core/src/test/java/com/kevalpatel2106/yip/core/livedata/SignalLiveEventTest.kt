package com.kevalpatel2106.yip.core.livedata

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
class SignalLiveEventTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var eventObserver: Observer<Unit>

    private val signalLiveEvent = SignalLiveEvent()

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
    fun testSendSignal() {
        signalLiveEvent.sendSignal()
        Mockito.verify(eventObserver, Mockito.times(1)).onChanged(Unit)
    }
}
