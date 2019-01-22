package com.kevalpatel2106.yip.repo.utils

import android.app.Application
import com.instacart.library.truetime.TrueTimeRx
import com.kevalpatel2106.testutils.MockSharedPreference
import com.kevalpatel2106.testutils.RxSchedulersOverrideRule
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class NtpProviderTest {
    private val mockSharedPreference = MockSharedPreference()
    private lateinit var ntpProvider: NtpProvider

    @Mock
    lateinit var application: Application

    @Rule
    @JvmField
    val rule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(application.getString(anyInt())).thenReturn("time.google.com")
        Mockito.`when`(application.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPreference)

        ntpProvider = NtpProvider(application)
    }

    @Test
    fun `check NTP Provider initialized`() {
        assertTrue(TrueTimeRx.isInitialized())
    }
}