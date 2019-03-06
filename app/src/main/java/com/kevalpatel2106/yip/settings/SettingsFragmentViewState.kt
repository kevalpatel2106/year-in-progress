package com.kevalpatel2106.yip.settings

import com.kevalpatel2106.yip.BuildConfig

data class SettingsFragmentViewState(
        val isBuyProClickable: Boolean,
        val isBuyProVisible: Boolean,
        val versionPreferenceSummary: String
) {
    companion object {
        fun initialState(): SettingsFragmentViewState {
            return SettingsFragmentViewState(
                    isBuyProClickable = false,
                    isBuyProVisible = false,
                    versionPreferenceSummary = BuildConfig.VERSION_NAME
            )
        }
    }
}