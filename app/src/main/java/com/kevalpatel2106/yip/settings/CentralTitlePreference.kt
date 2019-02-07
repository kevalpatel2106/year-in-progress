package com.kevalpatel2106.yip.settings

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder

internal class CentralTitlePreference @JvmOverloads constructor(
        context: Context,
        attributes: AttributeSet? = null,
        diffStyleAttributes: Int = 0
) : Preference(context, attributes, diffStyleAttributes){

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        (holder?.findViewById(android.R.id.title) as? AppCompatTextView)?.layoutParams?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        (holder?.findViewById(android.R.id.title) as? AppCompatTextView)?.gravity = Gravity.CENTER

        (holder?.findViewById(android.R.id.summary) as? AppCompatTextView)?.layoutParams?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        (holder?.findViewById(android.R.id.summary) as? AppCompatTextView)?.gravity = Gravity.CENTER
    }
}