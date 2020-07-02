package com.kevalpatel2106.yip.dashboard.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.kevalpatel2106.yip.dashboard.adapter.listItem.DeadlineListItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ListItemRepresentable

internal object DeadlineAdapterDiffCallback : DiffUtil.ItemCallback<ListItemRepresentable>() {

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: ListItemRepresentable,
        newItem: ListItemRepresentable
    ): Boolean {
        return if (oldItem is DeadlineListItem && newItem is DeadlineListItem) {
            oldItem.deadline.title == newItem.deadline.title &&
                    oldItem.deadline.percent == newItem.deadline.percent &&
                    oldItem.deadline.color == newItem.deadline.color
        } else {
            oldItem == newItem
        }
    }

    override fun areItemsTheSame(
        oldItem: ListItemRepresentable,
        newItem: ListItemRepresentable
    ): Boolean {
        return if (oldItem is DeadlineListItem && newItem is DeadlineListItem) {
            oldItem.deadline.id == newItem.deadline.id
        } else {
            oldItem == newItem
        }
    }
}
