package com.kevalpatel2106.yip.dashboard.adapter.listItem

import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.core.ext.emptyString
import com.kevalpatel2106.yip.dashboard.adapter.DeadlineAdapter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ErrorRepresentableTest {
    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }

    private lateinit var testMessage: String
    private val retryBlock = {}
    private lateinit var errorRepresentable: ErrorRepresentable

    @Before
    fun before() {
        testMessage = kFixture()
        errorRepresentable = ErrorRepresentable(testMessage, retryBlock)
    }

    @Test
    fun `when init check error message set`() {
        assertEquals(testMessage, errorRepresentable.message)
    }

    @Test
    fun `when init check retry action set`() {
        assertEquals(retryBlock, errorRepresentable.retry)
    }

    @Test
    fun `when comparing with other item with different message check both items not equal `() {
        // when
        val testWithOtherMessage = errorRepresentable.copy(message = kFixture())

        // check
        assertNotEquals(testWithOtherMessage, errorRepresentable)
    }

    @Test
    fun `when comparing with other item with different retry block check both items equal `() {
        // when
        val testWithOtherRetryBlock = errorRepresentable.copy(retry = { emptyString() })

        // check
        assertEquals(testWithOtherRetryBlock, errorRepresentable)
    }

    @Test
    fun `when comparing with other item with different object block check both items not equal `() {
        assertNotEquals(emptyString(), errorRepresentable)
        assertNotEquals(null, errorRepresentable)
    }

    @Test
    fun `check view holder type`() {
        assertEquals(DeadlineAdapter.TYPE_ERROR, errorRepresentable.type)
    }
}
