package com.kevalpatel2106.yip.dashboard

import com.kevalpatel2106.testutils.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class DashboardViewModelRateDialogTest : DashboardViewModelTestSetUp() {

    @Test
    fun `when rate now clicked check play store screen opens`() {
        // when
        model.onRateNowClicked()

        // Check
        assertEquals(OpenPlayStore, model.singleEvents.getOrAwaitValue())
    }

    @Test
    fun `when rate now clicked check never ask again preference value changes true`() {
        // when
        model.onRateNowClicked()

        // Check
        Mockito.verify(sharedPrefsProvider)
            .savePreferences(DashboardViewModel.PREF_KEY_NEVER_ASK_RATE, true)
    }

    @Test
    fun `when rate never clicked check never ask again preference value changes true`() {
        // when
        model.onRateNeverClicked()

        // Check
        Mockito.verify(sharedPrefsProvider)
            .savePreferences(DashboardViewModel.PREF_KEY_NEVER_ASK_RATE, true)
    }
}
