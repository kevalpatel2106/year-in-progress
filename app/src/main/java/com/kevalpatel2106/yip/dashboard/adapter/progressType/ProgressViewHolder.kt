package com.kevalpatel2106.yip.dashboard.adapter.progressType

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.recyclerview.viewholders.YipViewHolder
import com.kevalpatel2106.yip.dashboard.adapter.ProgressAdapterEventListener
import com.kevalpatel2106.yip.databinding.RowProgressBinding
import kotlinx.android.synthetic.main.row_progress.root_card


internal class ProgressViewHolder(
    private val binding: RowProgressBinding,
    private var listener: ProgressAdapterEventListener
) : YipViewHolder(binding.root) {

    fun bind(listItem: ProgressListItem) {
        binding.apply {
            progress = listItem.progress
            percentString = listItem.progressString
            percent = listItem.progress.percent.toInt()
        }
        root_card.background = listItem.backgroundGradient
        containerView.setOnClickListener { listener.onProgressClicked(listItem.progress) }
    }

    companion object {
        fun create(parent: ViewGroup, listener: ProgressAdapterEventListener): ProgressViewHolder {
            val binding = DataBindingUtil.inflate<RowProgressBinding>(
                LayoutInflater.from(parent.context),
                R.layout.row_progress,
                parent,
                false
            )
            return ProgressViewHolder(binding, listener)
        }
    }
}
