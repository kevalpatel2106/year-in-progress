package com.kevalpatel2106.yip.settings.preferenceList

import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.BuildConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SettingsFragmentInitalViewStateTest {
    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }

    @Test
    fun `when view state initialised check buy pro button click state`() {
        // when
        val initialState = SettingsFragmentViewState.initialState(kFixture())

        // then
        assertFalse(initialState.isBuyProClickable)
    }

    @Test
    fun `when view state initialised check buy pro button visibility state`() {
        // when
        val initialState = SettingsFragmentViewState.initialState(kFixture())

        // then
        assertFalse(initialState.isBuyProVisible)
    }

    @Test
    fun `when view state initialised check version summary`() {
        // when
        val initialState = SettingsFragmentViewState.initialState(kFixture())

        // then
        assertEquals(BuildConfig.VERSION_NAME, initialState.versionPreferenceSummary)
    }

    @Test
    fun `given initial dark mode value when view state initialised check dark mode `() {
        // given
        val initialDarkModeValue = kFixture<Int>()

        // when
        val initialState = SettingsFragmentViewState.initialState(initialDarkModeValue)

        // then
        assertFalse(initialState.isBuyProClickable)
    }
}
