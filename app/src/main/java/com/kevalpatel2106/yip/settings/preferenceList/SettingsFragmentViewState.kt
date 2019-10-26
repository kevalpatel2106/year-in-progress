package com.kevalpatel2106.yip.settings.preferenceList

import androidx.appcompat.app.AppCompatDelegate
import com.kevalpatel2106.yip.BuildConfig

data class SettingsFragmentViewState(
    val isBuyProClickable: Boolean,
    val isBuyProVisible: Boolean,
    val versionPreferenceSummary: String,
    val darkModeSettings: Int
) {
    companion object {
        fun initialState(): SettingsFragmentViewState {
            return SettingsFragmentViewState(
                isBuyProClickable = false,
                isBuyProVisible = false,
                versionPreferenceSummary = BuildConfig.VERSION_NAME,
                darkModeSettings = AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}
