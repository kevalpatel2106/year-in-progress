package com.kevalpatel2106.yip.core.recyclerview.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevalpatel2106.feature.core.R
import com.kevalpatel2106.yip.core.recyclerview.representable.EmptyRepresentable
import kotlinx.android.synthetic.main.row_list_empty_list.empty_list_item_text

internal class EmptyViewHolder(containerView: View) : YipViewHolder(containerView) {

    fun bind(emptyRepresentable: EmptyRepresentable) {
        empty_list_item_text.text = emptyRepresentable.message
    }

    companion object {
        fun create(parent: ViewGroup): EmptyViewHolder {
            return EmptyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_list_empty_list, parent, false)
            )
        }
    }
}
