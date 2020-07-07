package com.kevalpatel2106.yip.settings

import android.content.Context
import android.content.Intent
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.core.emptyString
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class SettingsUseCasePrepareShareIntentTest {
    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private val testString = kFixture<String>()

    @Mock
    internal lateinit var context: Context

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)

        whenever(context.getString(anyInt(), anyString(), anyString())).thenReturn(testString)
        whenever(context.getString(anyInt(), anyString())).thenReturn(testString)
        whenever(context.getString(anyInt())).thenReturn(emptyString())
    }

    @Test
    fun `when preparing share intent check intent action`() {
        val intent = SettingsUseCase.prepareShareIntent(context)
        assertEquals(Intent.ACTION_SEND, intent.action)
    }

    @Test
    fun `when preparing share intent check intent subject`() {
        val intent = SettingsUseCase.prepareShareIntent(context)
        assertEquals(testString, intent.getStringExtra(Intent.EXTRA_SUBJECT))
    }

    @Test
    fun `when preparing share intent check intent text`() {
        val intent = SettingsUseCase.prepareShareIntent(context)
        assertEquals(testString, intent.getStringExtra(Intent.EXTRA_TEXT))
    }

    @Test
    fun `when preparing share intent check intent type`() {
        val intent = SettingsUseCase.prepareShareIntent(context)
        assertEquals("text/plain", intent.type)
    }
}
