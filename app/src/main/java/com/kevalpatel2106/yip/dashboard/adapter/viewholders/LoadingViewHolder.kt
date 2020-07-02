package com.kevalpatel2106.yip.dashboard.adapter.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevalpatel2106.yip.R

internal class LoadingViewHolder(containerView: View) : DeadlineListViewHolder(containerView) {

    companion object {
        fun create(parent: ViewGroup): LoadingViewHolder {
            return LoadingViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_list_loading, parent, false)
            )
        }
    }
}
