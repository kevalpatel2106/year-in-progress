package com.kevalpatel2106.yip.settings.preferenceList

import com.kevalpatel2106.yip.BuildConfig
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SettingsFragmentViewStateTest {

    @Test
    fun checkInitialState() {
        val initialState = SettingsFragmentViewState.initialState()

        Assert.assertFalse(initialState.isBuyProClickable)
        Assert.assertFalse(initialState.isBuyProVisible)
        Assert.assertEquals(BuildConfig.VERSION_NAME, initialState.versionPreferenceSummary)
    }

    @Test
    fun checkEquals() {
        val state = SettingsFragmentViewState.initialState()
        val state1 = SettingsFragmentViewState.initialState()
        val state2 = SettingsFragmentViewState(
            false,
            false,
            "0.1.4"
        )

        Assert.assertEquals(state, state1)
        Assert.assertNotEquals(state, state2)
        Assert.assertNotEquals(state1, state2)
    }
}
