package com.kevalpatel2106.yip.detail

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class DetailViewStateInitialStateTest {
    private lateinit var initialState: DetailViewState

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var resources: Resources

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        whenever(context.resources).thenReturn(resources)
        whenever(resources.getDimension(anyInt())).thenReturn(0F)

        initialState = DetailViewState.initialState(context)
    }

    @Test
    fun `check initial value of start time text is empty string`() {
        assertTrue(initialState.startTimeText.isEmpty())
    }

    @Test
    fun `check initial value of end time text is empty string`() {
        assertTrue(initialState.endTimeText.isEmpty())
    }

    @Test
    fun `check initial value of show repeatable is false`() {
        assertFalse(initialState.showRepeatable)
    }

    @Test
    fun `check initial value of percent`() {
        assertEquals(0, initialState.percent)
    }

    @Test
    fun `check initial value of percent text`() {
        assertTrue(initialState.percentText.isEmpty())
    }

    @Test
    fun `check initial value of deadline color is gray`() {
        assertEquals(Color.GRAY, initialState.deadlineColor)
    }

    @Test
    fun `check initial value of deadline title is empty string`() {
        assertTrue(initialState.titleText.isEmpty())
    }

    @Test
    fun `check initial value of is deleting false`() {
        assertFalse(initialState.isDeleting)
    }

    @Test
    fun `check initial value of card background is not null`() {
        assertNotNull(initialState.cardBackground)
    }
}
