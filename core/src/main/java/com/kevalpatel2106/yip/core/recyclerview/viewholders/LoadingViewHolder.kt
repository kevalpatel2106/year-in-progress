package com.kevalpatel2106.yip.core.recyclerview.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevalpatel2106.feature.core.R

internal class LoadingViewHolder(containerView: View) : YipViewHolder(containerView) {

    companion object {
        fun create(parent: ViewGroup): LoadingViewHolder {
            return LoadingViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_list_loading, parent, false)
            )
        }
    }
}
