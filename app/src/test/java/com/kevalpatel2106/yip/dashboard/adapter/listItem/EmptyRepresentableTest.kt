package com.kevalpatel2106.yip.dashboard.adapter.listItem

import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.dashboard.adapter.DeadlineAdapter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EmptyRepresentableTest {
    private lateinit var testMessage: String
    private lateinit var emptyRepresentable: EmptyRepresentable

    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }

    @Before
    fun before() {
        testMessage = kFixture()
        emptyRepresentable = EmptyRepresentable(testMessage)
    }

    @Test
    fun `when init check value of the empty message`() {
        assertEquals(testMessage, emptyRepresentable.message)
    }

    @Test
    fun `check view holder type`() {
        assertEquals(DeadlineAdapter.TYPE_EMPTY, emptyRepresentable.type)
    }
}
