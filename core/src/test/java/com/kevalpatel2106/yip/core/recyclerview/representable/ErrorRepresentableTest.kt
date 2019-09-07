package com.kevalpatel2106.yip.core.recyclerview.representable

import com.kevalpatel2106.yip.core.emptyString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ErrorRepresentableTest {

    private val testMessage = "test"
    private val retryBlock = {}
    private val errorRepresentable = ErrorRepresentable(testMessage, retryBlock)

    @Test
    fun checkConstructor() {
        assertEquals(testMessage, errorRepresentable.message)
        assertEquals(retryBlock, errorRepresentable.retry)
    }

    @Test
    fun checkEquals() {
        val sameObject = ErrorRepresentable(
            testMessage,
            retryBlock
        )
        val testWithOtherMessage = errorRepresentable.copy(message = "test1")
        val testWithOtherRetryBlock = errorRepresentable.copy(retry = { emptyString() })

        assertEquals(sameObject, errorRepresentable)
        assertNotEquals(testWithOtherMessage, errorRepresentable)
        assertEquals(testWithOtherRetryBlock, errorRepresentable)

        assertNotEquals(emptyString(), errorRepresentable.hashCode())
        assertNotEquals(null, errorRepresentable.hashCode())
    }

    @Test
    fun checkHashcode() {
        val sameObject = ErrorRepresentable(
            testMessage,
            retryBlock
        )
        val testWithOtherMessage = errorRepresentable.copy(message = "test1")
        val testWithOtherRetryBlock = errorRepresentable.copy(retry = { emptyString() })

        assertEquals(sameObject.hashCode(), errorRepresentable.hashCode())
        assertNotEquals(testWithOtherMessage.hashCode(), errorRepresentable.hashCode())
        assertEquals(testWithOtherRetryBlock.hashCode(), errorRepresentable.hashCode())
    }
}
