package com.kevalpatel2106.yip.dashboard.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.kevalpatel2106.yip.recyclerview.representable.YipItemRepresentable

internal object ProgressAdapterDiffCallback : DiffUtil.ItemCallback<YipItemRepresentable>() {

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: YipItemRepresentable,
        newItem: YipItemRepresentable
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
        oldItem: YipItemRepresentable,
        newItem: YipItemRepresentable
    ): Boolean {
        return if (oldItem is ProgressListItem && newItem is ProgressListItem) {
            oldItem.progress.id == newItem.progress.id
        } else {
            oldItem == newItem
        }
    }
}
