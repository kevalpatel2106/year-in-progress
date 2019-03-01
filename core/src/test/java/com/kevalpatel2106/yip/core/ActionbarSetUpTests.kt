package com.kevalpatel2106.yip.core

import androidx.appcompat.app.ActionBar
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.*


@RunWith(Parameterized::class)
class ActionbarSetUpTests(private val isTitleEnabled: Boolean, private val isHomeEnabled: Boolean) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<Boolean>> {
            @Suppress("BooleanLiteralArgument")
            return arrayListOf(
                    arrayOf(true, true),
                    arrayOf(true, false),
                    arrayOf(false, true),
                    arrayOf(false, false)
            )
        }
    }

    @Captor
    lateinit var isTitleVisibleCaptor: ArgumentCaptor<Boolean>

    @Captor
    lateinit var homeEnabledCaptor: ArgumentCaptor<Boolean>

    @Captor
    lateinit var homeAsUpEnabledCaptor: ArgumentCaptor<Boolean>

    @Mock
    lateinit var mockActionBar: ActionBar

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this@ActionbarSetUpTests)
    }

    @Test
    fun checkActionbarSetUp() {
        mockActionBar.set(isHomeEnabled, isTitleEnabled)

        Mockito.verify(mockActionBar, Mockito.times(1)).setDisplayShowTitleEnabled(isTitleVisibleCaptor.capture())
        org.junit.Assert.assertEquals(isTitleEnabled, isTitleVisibleCaptor.value)

        Mockito.verify(mockActionBar, Mockito.times(1)).setDisplayShowHomeEnabled(homeEnabledCaptor.capture())
        org.junit.Assert.assertEquals(isHomeEnabled, homeEnabledCaptor.value)

        Mockito.verify(mockActionBar, Mockito.times(1)).setDisplayHomeAsUpEnabled(homeAsUpEnabledCaptor.capture())
        org.junit.Assert.assertEquals(isHomeEnabled, homeAsUpEnabledCaptor.value)
    }
}