package com.kevalpatel2106.yip.core


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Created by Keval on 02/06/18.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(JUnit4::class)
class SingleLiveEventTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var owner: LifecycleOwner

    @Mock
    private lateinit var eventObserver: Observer<Int>

    private val singleLiveEvent = SingleLiveEvent<Int>()

    private lateinit var lifecycle: LifecycleRegistry

    @Before
    @Throws(Exception::class)
    fun setUpLifecycles() {
        MockitoAnnotations.initMocks(this)

        // Link custom lifecycle owner with the lifecyle register.
        lifecycle = LifecycleRegistry(owner)
        `when`(owner.lifecycle).thenReturn(lifecycle)

        // Start observing
        singleLiveEvent.observe(owner, eventObserver)

        // Start in a non-active state
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    @Test
    fun valueNotSet_onFirstOnResume() {
        // On resume
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // no update should be emitted because no value has been set
        verify(eventObserver, never()).onChanged(anyInt())
    }

    @Test
    fun singleUpdate_onSecondOnResume_updatesOnce() {
        // After a value is set
        singleLiveEvent.value = 42

        // observers are called once on resume
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // on second resume, no update should be emitted.
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // Check that the observer is called once
        verify(eventObserver, times(1)).onChanged(anyInt())
    }

    @Test
    fun twoUpdates_updatesTwice() {
        // After a value is set
        singleLiveEvent.value = 42

        // observers are called once on resume
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // when the value is set again, observers are called again.
        singleLiveEvent.value = 23

        // Check that the observer has been called twice
        verify(eventObserver, times(2)).onChanged(anyInt())
    }

    @Test
    fun twoUpdates_noUpdateUntilActive() {
        // Set a value
        singleLiveEvent.value = 42

        // which doesn't emit a change
        verify(eventObserver, never()).onChanged(42)

        // and set it again
        singleLiveEvent.value = 42

        // observers are called once on resume.
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // Check that the observer is called only once
        verify(eventObserver, times(1)).onChanged(anyInt())
    }
}