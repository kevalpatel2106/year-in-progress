package com.kevalpatel2106.yip.dashboard.navDrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kevalpatel2106.yip.BuildConfig
import com.kevalpatel2106.yip.DebugActivity
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.openPlayStorePage
import com.kevalpatel2106.yip.core.sendMailToDev
import com.kevalpatel2106.yip.core.showOrHide
import com.kevalpatel2106.yip.settings.SettingsActivity
import kotlinx.android.synthetic.main.dialog_bottom_navigation.navigation_drawer_option_debug
import kotlinx.android.synthetic.main.dialog_bottom_navigation.navigation_drawer_option_feedback
import kotlinx.android.synthetic.main.dialog_bottom_navigation.navigation_drawer_option_rate
import kotlinx.android.synthetic.main.dialog_bottom_navigation.navigation_drawer_option_settings

internal class BottomNavigationDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_bottom_navigation, container, false)
    }

    override fun getTheme(): Int = R.style.RoundedBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation_drawer_option_settings.setOnClickListener {
            SettingsActivity.launch(requireContext())
            dismiss()
        }
        navigation_drawer_option_feedback.setOnClickListener {
            context?.sendMailToDev()
            dismiss()
        }
        navigation_drawer_option_rate.setOnClickListener {
            context?.openPlayStorePage()
            dismiss()
        }
        setDebugOptions()
    }

    private fun setDebugOptions() {
        navigation_drawer_option_debug.showOrHide(BuildConfig.DEBUG)
        navigation_drawer_option_debug.setOnClickListener { DebugActivity.launch(requireContext()) }
    }
}
