package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.generateDeadline
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class EditViewDeadlineTitleTest : EditViewDeadlineModelTestSetUp() {

    private lateinit var deadline: Deadline

    @Before
    fun setUp() {
        deadline = generateDeadline(kFixture)
        deadlineSubject.onNext(deadline)
        viewModel.setDeadlineId(deadline.id)
    }

    @Test
    fun `given new title same as initial title with spaces when title changed check nothing change`() {
        // given
        val newTitle = "${viewModel.viewState.getOrAwaitValue().initialTitle}   "

        // when
        viewModel.onTitleChanged(newTitle)

        // then
        assertFalse(viewModel.isSomethingChanged)
    }

    @Test
    fun `given new title is valid when title changed check current title change in view state`() {
        // given
        val newTitle = "${kFixture<String>()}    "
        whenever(validator.isValidTitle(newTitle)).thenReturn(true)

        // when
        viewModel.onTitleChanged(newTitle)

        // then
        assertEquals(newTitle, viewModel.viewState.getOrAwaitValue().currentTitle)
    }

    @Test
    fun `given new title is valid when title changed check something changes true`() {
        // given
        val newTitle = kFixture<String>()
        whenever(validator.isValidTitle(newTitle)).thenReturn(true)

        // when
        viewModel.onTitleChanged(newTitle)

        // then
        assertTrue(viewModel.isSomethingChanged)
    }

    @Test
    fun `given new title is valid when title changed check title error message null`() {
        // given
        val newTitle = kFixture<String>()
        whenever(validator.isValidTitle(newTitle)).thenReturn(true)

        // when
        viewModel.onTitleChanged(newTitle)

        // then
        assertNull(viewModel.viewState.getOrAwaitValue().titleErrorMsg)
    }

    @Test
    fun `given new title is invalid with spaces when title changed check title error message displayed`() {
        // given
        val newTitle = kFixture<String>()
        whenever(validator.isValidTitle(newTitle)).thenReturn(false)

        // when
        viewModel.onTitleChanged(newTitle)

        // then
        assertEquals(testString, viewModel.viewState.getOrAwaitValue().titleErrorMsg)

    }
}
