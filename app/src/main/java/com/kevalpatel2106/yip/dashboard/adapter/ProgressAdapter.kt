package com.kevalpatel2106.yip.dashboard.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.recyclerview.YipAdapter
import com.kevalpatel2106.yip.recyclerview.representable.YipItemRepresentable
import com.kevalpatel2106.yip.recyclerview.viewholders.YipViewHolder

internal class ProgressAdapter(
        private var clickListener: ((progress: Progress) -> Unit)? = null
) : YipAdapter(DIFF_CALLBACK) {

    init {
        setHasStableIds(true)
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): YipViewHolder {
        return when (viewType) {
            PROGRESS_BAR_TYPE -> ProgressViewHolder.create(parent)
            ADS_TYPE -> AdsViewHolder.create(parent)
            PADDING_TYPE -> PaddingViewHolder.create(parent)
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun bindViewHolder(holder: YipViewHolder, item: YipItemRepresentable) {
        when (holder) {
            is ProgressViewHolder -> {
                (item as? ProgressListItem)?.let { holder.bind(it.progress, clickListener) }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        val item = getItem(position)
        return if (item is ProgressListItem) {
            item.progress.id
        } else {
            (item.type * position).toLong()
        }
    }

    companion object {
        internal const val PROGRESS_BAR_TYPE = 34533
        internal const val ADS_TYPE = 546
        internal const val PADDING_TYPE = 2345

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<YipItemRepresentable>() {

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                    oldItem: YipItemRepresentable,
                    newItem: YipItemRepresentable
            ): Boolean {
                return if (oldItem is ProgressListItem && newItem is ProgressListItem) {
                    oldItem.progress.title == newItem.progress.title
                            && oldItem.progress.percent == newItem.progress.percent
                            && oldItem.progress.color == newItem.progress.color
                } else {
                    oldItem == newItem
                }
            }

            override fun areItemsTheSame(oldItem: YipItemRepresentable, newItem: YipItemRepresentable): Boolean {
                return if (oldItem is ProgressListItem && newItem is ProgressListItem) {
                    oldItem.progress.id == newItem.progress.id
                } else {
                    oldItem == newItem
                }
            }
        }
    }
}