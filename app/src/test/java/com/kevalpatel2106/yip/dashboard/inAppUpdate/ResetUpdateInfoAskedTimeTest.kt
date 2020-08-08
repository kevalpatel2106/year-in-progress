package com.kevalpatel2106.yip.dashboard.inAppUpdate

import com.kevalpatel2106.testutils.assertMilliseconds
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ResetUpdateInfoAskedTimeTest {

    @Mock
    lateinit var prefsProvider: SharedPrefsProvider

    private lateinit var appUpdateHelper: InAppUpdateHelper

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        appUpdateHelper = InAppUpdateHelperImpl(prefsProvider)
    }

    @Test
    fun `when resting update info asked time called check preference value`() {
        // when
        appUpdateHelper.resetUpdateInfoAskedTime()

        // check
        val timeCaptor = argumentCaptor<Long>()
        verify(prefsProvider).savePreferences(ArgumentMatchers.anyString(), timeCaptor.capture())
        assertMilliseconds(System.currentTimeMillis(), timeCaptor.lastValue)
    }
}
