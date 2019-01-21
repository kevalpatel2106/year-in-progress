package com.kevalpatel2106.yip.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.kevalpatel2106.yip.BuildConfig
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.nullSafeObserve
import com.kevalpatel2106.yip.core.sendMailToDev
import com.kevalpatel2106.yip.di.getAppComponent
import com.kevalpatel2106.yip.payment.PaymentActivity
import javax.inject.Inject

internal class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var model: SettingsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.getAppComponent().inject(this@SettingsFragment)
        model = ViewModelProviders
            .of(this@SettingsFragment, viewModelProvider)
            .get(SettingsViewModel::class.java)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        activity?.let { model.refreshPurchaseState(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreference<Preference>(getString(R.string.pref_key_version)).summary = BuildConfig.VERSION_NAME

        // Set up buy pro option
        val buyProPref = findPreference<Preference>(getString(R.string.pref_key_buy_pro))
        model.isCheckingPurchases.nullSafeObserve(this@SettingsFragment) { buyProPref.isEnabled = !it }
        model.isPurchased.nullSafeObserve(this@SettingsFragment) { buyProPref.isVisible = !it }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.pref_key_buy_pro) -> context?.let { PaymentActivity.launch(it) }
            getString(R.string.pref_key_contact) -> context?.sendMailToDev()
        }
        return super.onPreferenceTreeClick(preference)
    }

    companion object {
        internal fun getNewInstance() = SettingsFragment()
    }
}
