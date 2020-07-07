package com.kevalpatel2106.yip.settings.preferenceList

import com.kevalpatel2106.yip.BuildConfig
import com.kevalpatel2106.yip.repo.nightModeRepo.NightModeValue

data class SettingsFragmentViewState(
    val isBuyProClickable: Boolean,
    val isBuyProVisible: Boolean,
    val versionPreferenceSummary: String,
    @NightModeValue val darkModeValue: Int
) {
    companion object {
        fun initialState(@NightModeValue darkModeValue: Int): SettingsFragmentViewState {
            return SettingsFragmentViewState(
                isBuyProClickable = false,
                isBuyProVisible = false,
                versionPreferenceSummary = BuildConfig.VERSION_NAME,
                darkModeValue = darkModeValue
            )
        }
    }
}
