package com.kevalpatel2106.yip.core

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by Keval on 02/06/18.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(JUnit4::class)
class LiveDataExtTest {
    private val testString = "test"

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var testLiveData: MutableLiveData<String>
    @Mock
    private lateinit var eventObserver: Observer<String>


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this@LiveDataExtTest)

        testLiveData = MutableLiveData()
        testLiveData.value = testString
    }

    @Test
    fun checkRecall() {
        testLiveData.observeForever(eventObserver)

        testLiveData.recall()

        Mockito.verify(eventObserver, Mockito.times(2))
                .onChanged(testString)
    }
}