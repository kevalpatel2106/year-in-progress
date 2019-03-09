package com.kevalpatel2106.yip.recyclerview.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevalpatel2106.yip.recyclerview.R
import com.kevalpatel2106.yip.recyclerview.representable.EmptyRepresentable
import kotlinx.android.synthetic.main.row_list_empty_list.empty_list_item_text


internal class EmptyViewHolder(containerView: View) : YipViewHolder(containerView) {
    override fun isDragSupported(): Boolean = false

    fun bind(emptyRepresentable: EmptyRepresentable) {
        empty_list_item_text.text = emptyRepresentable.message
    }

    companion object {
        fun create(parent: ViewGroup) = EmptyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_list_empty_list, parent, false)
        )
    }
}