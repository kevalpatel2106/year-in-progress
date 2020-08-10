package com.kevalpatel2106.yip.settings.preferenceList

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.core.sendMailToDev
import com.kevalpatel2106.yip.payment.PaymentActivity
import com.kevalpatel2106.yip.settings.SettingsUseCase
import com.kevalpatel2106.yip.webviews.WebViewLaunchContent.ADD_WIDGET
import com.kevalpatel2106.yip.webviews.WebViewLaunchContent.CHANGELOG
import com.kevalpatel2106.yip.webviews.WebViewLaunchContent.PRIVACY_POLICY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class SettingsFragment : PreferenceFragmentCompat() {

    private val model: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the sort sortOrder
        findPreference<ListPreference>(getString(R.string.pref_key_order))?.summaryProvider =
            ListPreference.SimpleSummaryProvider.getInstance()

        // Set the dark mode
        findPreference<ListPreference>(getString(R.string.pref_key_dark_mode))?.summaryProvider =
            ListPreference.SimpleSummaryProvider.getInstance()

        // Set the date selector
        findPreference<ListPreference>(getString(R.string.pref_key_date_format))?.summaryProvider =
            ListPreference.SimpleSummaryProvider.getInstance()

        // Set the time selector
        findPreference<ListPreference>(getString(R.string.pref_key_time_format))?.summaryProvider =
            ListPreference.SimpleSummaryProvider.getInstance()

        monitorState()
    }

    private fun monitorState() {
        val versionPref = findPreference<Preference>(getString(R.string.pref_key_version))
        val buyProPref = findPreference<Preference>(getString(R.string.pref_key_buy_pro))
        val buyProPrefHeader =
            findPreference<PreferenceCategory>(getString(R.string.pref_key_pro_version_header))

        model.viewState.nullSafeObserve(this@SettingsFragment) {
            versionPref?.summary = it.versionPreferenceSummary
            buyProPref?.isEnabled = it.isBuyProClickable
            buyProPrefHeader?.isVisible = it.isBuyProVisible

            if (AppCompatDelegate.getDefaultNightMode() != it.darkModeValue) {
                AppCompatDelegate.setDefaultNightMode(it.darkModeValue)
            }
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.pref_key_buy_pro) -> context?.let { PaymentActivity.launch(it) }
            getString(R.string.pref_key_contact) -> context?.sendMailToDev()
            getString(R.string.pref_key_add_widget) -> {
                SettingsUseCase.openWebPage(requireContext(), ADD_WIDGET)
            }
            getString(R.string.pref_key_privacy_policy) -> {
                SettingsUseCase.openWebPage(requireContext(), PRIVACY_POLICY)
            }
            getString(R.string.pref_key_changelog) -> {
                SettingsUseCase.openWebPage(requireContext(), CHANGELOG)
            }
            getString(R.string.pref_key_open_source_licences) -> {
                SettingsUseCase.showLibraryLicences(requireContext())
            }
            getString(R.string.pref_key_share_friends) -> {
                startActivity(SettingsUseCase.prepareShareIntent(requireContext()))
            }
        }
        return super.onPreferenceTreeClick(preference)
    }
}
