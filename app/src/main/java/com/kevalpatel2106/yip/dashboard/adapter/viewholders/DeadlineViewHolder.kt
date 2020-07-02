package com.kevalpatel2106.yip.dashboard.adapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.dashboard.adapter.DeadlineAdapterEventListener
import com.kevalpatel2106.yip.dashboard.adapter.listItem.DeadlineListItem
import com.kevalpatel2106.yip.databinding.RowDeadlineBinding
import kotlinx.android.synthetic.main.row_deadline.root_card

internal class DeadlineViewHolder(
    private val binding: RowDeadlineBinding,
    private var listener: DeadlineAdapterEventListener
) : DeadlineListViewHolder(binding.root) {

    fun bind(listItem: DeadlineListItem) {
        binding.apply {
            deadlineName = listItem.deadline.title
            percentString = listItem.deadlineString
            percent = listItem.deadline.percent.toInt()
        }
        root_card.background = listItem.backgroundGradient
        containerView.setOnClickListener { listener.onDeadlineClicked(listItem.deadline) }
    }

    companion object {
        fun create(parent: ViewGroup, listener: DeadlineAdapterEventListener): DeadlineViewHolder {
            val binding = DataBindingUtil.inflate<RowDeadlineBinding>(
                LayoutInflater.from(parent.context),
                R.layout.row_deadline,
                parent,
                false
            )
            return DeadlineViewHolder(binding, listener)
        }
    }
}
