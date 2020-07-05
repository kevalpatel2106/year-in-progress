package com.kevalpatel2106.yip.core.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations

/**
 * Created by Keval on 02/06/18.
 */
@RunWith(JUnit4::class)
class LiveDataExtNullSafeObserverTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private val testLiveData = MutableLiveData<String>()
    private val testString = kFixture<String>()
    private var lifecycleOwner: LifecycleOwner = LifecycleOwner {
        object : Lifecycle() {
            override fun addObserver(observer: LifecycleObserver) = Unit
            override fun removeObserver(observer: LifecycleObserver) = Unit
            override fun getCurrentState(): State = State.CREATED
        }
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this@LiveDataExtNullSafeObserverTest)
    }

    @Test
    fun `when null value set to null safe observer check on change never called`() {
        testLiveData.nullSafeObserve(lifecycleOwner) { fail() }
        testLiveData.value = null
    }

    @Test
    fun `when non-null value set to null safe observer check on change called`() {
        testLiveData.nullSafeObserve(lifecycleOwner) { assertEquals(testString, it) }
        testLiveData.value = testString
    }
}
