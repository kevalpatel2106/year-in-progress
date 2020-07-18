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
class EditViewStateIsDescritionChangedTest {

    @Mock
    lateinit var dateFormatter: DateFormatter

    private val description1 = "initial description"
    private val description2 = "current description"

    private lateinit var initialState: EditViewState

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        initialState = EditViewState.initialState(dateFormatter)
    }

    @Test
    fun `given initial and current description different check is title changed true`() {
        val state = initialState.copy(
            initialDescription = description1,
            currentDescription = description2
        )
        assertTrue(state.isDescriptionChanged())
    }

    @Test
    fun `given initial and current description same check is title changed false`() {
        val state = initialState.copy(
            initialDescription = description1,
            currentDescription = description1
        )
        assertFalse(state.isDescriptionChanged())
    }

    @Test
    fun `given initial and current description same with white spaces check is title changed false`() {
        val state = initialState.copy(
            initialDescription = description1,
            currentDescription = "$description1  "
        )
        assertFalse(state.isDescriptionChanged())
    }
}
