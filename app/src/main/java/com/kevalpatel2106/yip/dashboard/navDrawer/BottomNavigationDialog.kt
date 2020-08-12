package com.kevalpatel2106.yip.dashboard.navDrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kevalpatel2106.yip.BuildConfig
import com.kevalpatel2106.yip.DebugActivity
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.ext.sendMailToDev
import com.kevalpatel2106.yip.core.ext.showOrHide
import com.kevalpatel2106.yip.databinding.DialogBottomNavigationBinding
import com.kevalpatel2106.yip.settings.SettingsActivity
import kotlinx.android.synthetic.main.dialog_bottom_navigation.navigation_drawer_option_debug
import kotlinx.android.synthetic.main.dialog_bottom_navigation.navigation_drawer_option_feedback
import kotlinx.android.synthetic.main.dialog_bottom_navigation.navigation_drawer_option_settings

internal class BottomNavigationDialog : BottomSheetDialogFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil
            .inflate<DialogBottomNavigationBinding>(
                inflater,
                R.layout.dialog_bottom_navigation,
                container,
                false
            )
            .apply {
                lifecycleOwner = this@BottomNavigationDialog
                clickHandler = this@BottomNavigationDialog
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation_drawer_option_debug.showOrHide(BuildConfig.DEBUG)
    }

    override fun onClick(viewClicked: View?) {
        when (viewClicked) {
            navigation_drawer_option_settings -> SettingsActivity.launch(requireContext())
            navigation_drawer_option_feedback -> context?.sendMailToDev()
            navigation_drawer_option_debug -> DebugActivity.launch(requireContext())
        }
        dismiss()
    }
}
