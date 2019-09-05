package com.kevalpatel2106.yip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import io.palaima.debugdrawer.base.DebugModuleAdapter

class BillingModule(private val changeListener: (isPro: Boolean) -> Unit) : DebugModuleAdapter() {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View {
        val view = inflater.inflate(R.layout.module_pro_switch, parent, false)

        view.findViewById<SwitchMaterial>(R.id.debug_pro_switch)
            .setOnCheckedChangeListener { _, isOn ->
                changeListener(isOn)
            }

        return view
    }
}
