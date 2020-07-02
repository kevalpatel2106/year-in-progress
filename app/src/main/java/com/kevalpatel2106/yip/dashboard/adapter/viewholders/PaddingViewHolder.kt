package com.kevalpatel2106.yip.dashboard.adapter.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevalpatel2106.yip.R

internal class PaddingViewHolder(itemView: View) : DeadlineListViewHolder(itemView) {

    companion object {
        fun create(parent: ViewGroup): PaddingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_padding_bottom, parent, false)
            return PaddingViewHolder(view)
        }
    }
}
