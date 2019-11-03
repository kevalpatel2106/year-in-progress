package com.kevalpatel2106.yip.core.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by Keval on 02/06/18.
 */
@RunWith(JUnit4::class)
class LiveDataExtTest {
    private val testString = "test"

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var eventObserver: Observer<String>

    @Captor
    private lateinit var stringCaptor: ArgumentCaptor<String>

    private val testLiveData = MutableLiveData<String>()

    private var lifecycleOwner: LifecycleOwner = LifecycleOwner {
        object : Lifecycle() {
            override fun addObserver(observer: LifecycleObserver) = Unit
            override fun removeObserver(observer: LifecycleObserver) = Unit
            override fun getCurrentState(): State = State.CREATED
        }
    }


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this@LiveDataExtTest)
    }

    @Test
    fun checkNullSafeObserver() {
        testLiveData.nullSafeObserve(lifecycleOwner) {
            assertNotNull(it)
            assertEquals(testString, it)
        }

        testLiveData.value = testString
        testLiveData.value = null
    }

    @Test
    fun checkRecall() {
        testLiveData.value = testString
        testLiveData.observeForever(eventObserver)

        testLiveData.recall()
        Mockito.verify(eventObserver, Mockito.times(2))
            .onChanged(stringCaptor.capture())
        assertEquals(testString, stringCaptor.value)

        testLiveData.removeObserver(eventObserver)
    }
}
