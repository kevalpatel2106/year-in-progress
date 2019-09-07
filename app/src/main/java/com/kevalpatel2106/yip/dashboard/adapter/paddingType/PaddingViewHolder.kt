package com.kevalpatel2106.yip.dashboard.adapter.paddingType

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.recyclerview.viewholders.YipViewHolder

internal class PaddingViewHolder(itemView: View) : YipViewHolder(itemView) {

    companion object {
        fun create(parent: ViewGroup): PaddingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_padding_bottom, parent, false)
            return PaddingViewHolder(view)
        }
    }
}
