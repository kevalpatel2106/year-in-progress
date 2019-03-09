package com.kevalpatel2106.yip.recyclerview.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevalpatel2106.yip.recyclerview.R


internal class LoadingViewHolder(containerView: View) : YipViewHolder(containerView) {
    override fun isDragSupported(): Boolean = false

    companion object {
        fun create(parent: ViewGroup) = LoadingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_list_loading, parent, false)
        )
    }
}
