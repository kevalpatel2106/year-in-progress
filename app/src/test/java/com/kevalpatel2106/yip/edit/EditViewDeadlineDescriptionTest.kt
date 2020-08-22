package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.generateDeadline
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class EditViewDeadlineDescriptionTest : EditViewDeadlineModelTestSetUp() {

    private lateinit var deadline: Deadline

    @Before
    fun setUp() {
        deadline = generateDeadline(kFixture)
        deadlineSubject.onNext(deadline)
        viewModel.setDeadlineId(deadline.id)
    }

    @Test
    fun `given new description same as initial description with spaces when description changed check nothing change`() {
        // given
        val newDescription = "${viewModel.viewState.getOrAwaitValue().initialDescription}   "

        // when
        viewModel.onDescriptionChanged(newDescription)

        // then
        assertFalse(viewModel.isSomethingChanged)
    }

    @Test
    fun `given new description when description changed check current description change in view state`() {
        // given
        val newDescription = "${kFixture<String>()}    "

        // when
        viewModel.onDescriptionChanged(newDescription)

        // then
        assertEquals(newDescription, viewModel.viewState.getOrAwaitValue().currentDescription)
    }

    @Test
    fun `given new description when description changed check something changes true`() {
        // given
        val newDescription = kFixture<String>()

        // when
        viewModel.onDescriptionChanged(newDescription)

        // then
        assertTrue(viewModel.isSomethingChanged)
    }
}
