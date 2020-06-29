package com.kevalpatel2106.yip.dashboard.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ListItemRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ProgressListItem

internal object ProgressAdapterDiffCallback : DiffUtil.ItemCallback<ListItemRepresentable>() {

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: ListItemRepresentable,
        newItem: ListItemRepresentable
    ): Boolean {
        return if (oldItem is ProgressListItem && newItem is ProgressListItem) {
            oldItem.progress.title == newItem.progress.title &&
                    oldItem.progress.percent == newItem.progress.percent &&
                    oldItem.progress.color == newItem.progress.color
        } else {
            oldItem == newItem
        }
    }

    override fun areItemsTheSame(
        oldItem: ListItemRepresentable,
        newItem: ListItemRepresentable
    ): Boolean {
        return if (oldItem is ProgressListItem && newItem is ProgressListItem) {
            oldItem.progress.id == newItem.progress.id
        } else {
            oldItem == newItem
        }
    }
}
