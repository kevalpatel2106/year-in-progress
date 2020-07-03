package com.kevalpatel2106.yip.dashboard.adapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.dashboard.adapter.listItem.EmptyRepresentable
import com.kevalpatel2106.yip.databinding.RowEmptyListBinding

internal class EmptyViewHolder(
    private val binding: RowEmptyListBinding
) : DeadlineListViewHolder(binding.root) {

    fun bind(emptyRepresentable: EmptyRepresentable) {
        binding.emptyMessage = emptyRepresentable.message
    }

    companion object {
        fun create(parent: ViewGroup): EmptyViewHolder {
            val binding = DataBindingUtil.inflate<RowEmptyListBinding>(
                LayoutInflater.from(parent.context),
                R.layout.row_empty_list,
                parent,
                false
            )
            return EmptyViewHolder(binding)
        }
    }
}
