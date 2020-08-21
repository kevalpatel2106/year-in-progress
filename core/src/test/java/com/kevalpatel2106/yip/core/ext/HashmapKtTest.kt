package com.kevalpatel2106.yip.core.ext

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HashmapKtTest {

    @Test
    fun `given value in hashmap when get first key called check key returned`() {
        // given
        val hashMap = hashMapOf("One" to 1, "Two" to 2, "Three" to 3)

        // when
        val key = hashMap.getFirstKey(3)

        // check
        assertEquals("Three", key)
    }

    @Test
    fun `given two same value in hashmap when get first key called check  first key returned`() {
        // given
        val hashMap = hashMapOf("One" to 1, "Two" to 2, "Three" to 3, "Three1" to 3)

        // when
        val key = hashMap.getFirstKey(3)

        // check
        assertEquals("Three", key)
    }

    @Test
    fun `given value not in hashmap when get first key called check error occured`() {
        // given
        val hashMap = hashMapOf("One" to 1, "Two" to 2, "Three" to 3)

        // when
        try {
            val key = hashMap.getFirstKey(5)
            fail()
        } catch (e: IllegalArgumentException) {
            assertNotNull(e.message)
        }
    }
}
