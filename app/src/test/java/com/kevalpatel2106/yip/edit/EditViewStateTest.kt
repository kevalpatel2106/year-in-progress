package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatter
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class EditViewStateTest {

    @Mock
    lateinit var dateFormatter: DateFormatter

    private val title1 = "initial title"
    private val title2 = "current title"

    private lateinit var initialState: EditViewState

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        initialState = EditViewState.initialState(dateFormatter)
    }

    @Test
    fun `given initial and current title different check is title changed true`() {
        val state = initialState.copy(initialTitle = title1, currentTitle = title2)
        assertTrue(state.isTitleChanged())
    }

    @Test
    fun `given initial and current title same check is title changed false`() {
        val state = initialState.copy(initialTitle = title1, currentTitle = title1)
        assertFalse(state.isTitleChanged())
    }

    @Test
    fun `given initial and current title same with white spaces check is title changed false`() {
        val state = initialState.copy(initialTitle = title1, currentTitle = "$title1  ")
        assertFalse(state.isTitleChanged())
    }
}
