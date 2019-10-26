package com.kevalpatel2106.yip.settings.preferenceList

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.di.provideViewModel
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.core.sendMailToDev
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.payment.PaymentActivity
import com.kevalpatel2106.yip.settings.SettingsUseCase
import com.kevalpatel2106.yip.webviews.WebViewActivity
import javax.inject.Inject


internal class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private val model: SettingsViewModel by lazy {
        provideViewModel(viewModelProvider, SettingsViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.getAppComponent().inject(this@SettingsFragment)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { model.refreshPurchaseState(it) }

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
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.pref_key_buy_pro) -> context?.let { PaymentActivity.launch(it) }
            getString(R.string.pref_key_contact) -> context?.sendMailToDev()
            getString(R.string.pref_key_add_widget) -> {
                context?.let { WebViewActivity.showWidgetGuide(it) }
            }
            getString(R.string.pref_key_privacy_policy) -> {
                context?.let { WebViewActivity.showPrivacyPolicy(it) }
            }
            getString(R.string.pref_key_changelog) -> {
                context?.let { WebViewActivity.showChangelog(it) }
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


    companion object {
        internal fun getNewInstance() = SettingsFragment()
    }
}
