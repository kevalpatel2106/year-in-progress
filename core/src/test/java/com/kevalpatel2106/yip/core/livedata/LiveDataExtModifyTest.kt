package com.kevalpatel2106.yip.core.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LiveDataExtModifyTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }

    private val testLiveData = MutableLiveData<String>()
    private val initialString = kFixture<String>()
    private val modifiedString = kFixture<String>()


    @Test
    fun `given initial value non-null when modify with new value check value updated`() {
        // given
        testLiveData.value = initialString

        // when
        testLiveData.modify { modifiedString }

        // check
        assertEquals(modifiedString, testLiveData.value)
    }

    @Test
    fun `given initial value null when modify with new value check value not updated`() {
        // given
        testLiveData.value = null

        // when
        testLiveData.modify { modifiedString }

        // check
        assertNull(testLiveData.value)
    }
}
