package com.kevalpatel2106.yip.core.recyclerview.representable

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EmptyRepresentableTest {
    private val testMessage = "test message"
    private val emptyRepresentable = EmptyRepresentable(testMessage)

    @Test
    fun checkConstructor() {
        Assert.assertEquals(testMessage, emptyRepresentable.message)
    }

    @Test
    fun checkEquals() {
        val otherMessage =
            EmptyRepresentable("test1")
        val sameMessage =
            EmptyRepresentable(testMessage)

        Assert.assertNotEquals(null, emptyRepresentable)
        Assert.assertNotEquals(234L, emptyRepresentable)
        Assert.assertNotEquals(otherMessage, emptyRepresentable)
        Assert.assertEquals(sameMessage, emptyRepresentable)
    }

    @Test
    fun checkHashcode() {
        val otherMessage =
            EmptyRepresentable("test1")
        val sameMessage =
            EmptyRepresentable(testMessage)

        Assert.assertNotEquals(otherMessage.hashCode(), emptyRepresentable.hashCode())
        Assert.assertEquals(sameMessage.hashCode(), emptyRepresentable.hashCode())
    }
}
