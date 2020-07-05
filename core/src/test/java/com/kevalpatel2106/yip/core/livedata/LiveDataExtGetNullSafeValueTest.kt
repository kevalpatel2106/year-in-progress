package com.kevalpatel2106.yip.core.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LiveDataExtGetNullSafeValueTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }

    private val testLiveData = MutableLiveData<String>()
    private val testValue = kFixture<String>()


    @Test
    fun `given value set to non-null when get null safe value called check value`() {
        // given
        testLiveData.value = testValue

        // when
        val checkValue = testLiveData.nullSafeValue()

        // check
        assertEquals(testValue, checkValue)
    }

    @Test
    fun `given value set to non-null when get null safe value called exception`() {
        // given
        testLiveData.value = null

        // when - check
        try {
            testLiveData.nullSafeValue()
            fail()
        } catch (e: IllegalArgumentException) {
            assertNotNull(e.message)
        }
    }
}
