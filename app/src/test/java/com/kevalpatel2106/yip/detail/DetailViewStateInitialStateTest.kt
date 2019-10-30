package com.kevalpatel2106.yip.detail

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import com.kevalpatel2106.yip.core.emptyString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
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
        Mockito.`when`(context.resources).thenReturn(resources)
        Mockito.`when`(resources.getDimension(anyInt())).thenReturn(0F)

        initialState = DetailViewState.initialState(context)
    }

    @Test
    fun checkInitialState() {
        assertEquals(emptyString(), initialState.progressEndTimeText)
        assertNotNull(initialState.cardBackground)
        assertEquals(ProgressFlipper.POS_TIME_LEFT, initialState.detailFlipperPosition)
        assertFalse(initialState.isDeletingProgress)
        assertFalse(initialState.showRepeatable)
        assertEquals(0, initialState.progressPercent)
        assertEquals(emptyString(), initialState.progressPercentText)
        assertEquals(Color.GRAY, initialState.progressColor)
        assertEquals(emptyString(), initialState.progressTitleText)
    }

}
