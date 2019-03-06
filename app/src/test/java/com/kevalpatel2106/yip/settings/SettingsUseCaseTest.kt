package com.kevalpatel2106.yip.settings

import android.content.Context
import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class SettingsUseCases {
    private val testString = "testString"

    @Mock
    internal lateinit var context: Context

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this@SettingsUseCases)
        Mockito.`when`(context.getString(anyInt(), anyString(), anyString())).thenReturn(testString)
        Mockito.`when`(context.getString(anyInt(), anyString())).thenReturn(testString)
        Mockito.`when`(context.getString(anyInt())).thenReturn("")
    }

    @Test
    fun checkPrepareShareIntent_whenContextNotNull() {
        val intent = SettingsUseCase.prepareShareIntent(context)
        assertNotNull(intent)
    }

    @Test
    fun checkPrepareShareIntent_whenContextNull() {
        val intent = SettingsUseCase.prepareShareIntent(null)
        assertNull(intent)
    }

    @Test
    fun checkPrepareShareIntentAction_whenContextNotNull() {
        val intent = SettingsUseCase.prepareShareIntent(context)
        assertEquals(Intent.ACTION_SEND, intent?.action)
    }

    @Test
    fun checkPrepareShareIntentSubject_whenContextNotNull() {
        val intent = SettingsUseCase.prepareShareIntent(context)
        assertEquals(testString, intent?.getStringExtra(Intent.EXTRA_SUBJECT))
    }

    @Test
    fun checkPrepareShareIntentText_whenContextNotNull() {
        val intent = SettingsUseCase.prepareShareIntent(context)
        assertEquals(testString, intent?.getStringExtra(Intent.EXTRA_TEXT))
    }

    @Test
    fun checkPrepareShareIntentType_whenContextNotNull() {
        val intent = SettingsUseCase.prepareShareIntent(context)
        assertEquals("text/plain", intent?.type)
    }
}