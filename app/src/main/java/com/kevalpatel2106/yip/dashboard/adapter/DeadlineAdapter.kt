package com.kevalpatel2106.yip.dashboard.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kevalpatel2106.yip.dashboard.adapter.listItem.DeadlineListItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.EmptyRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ErrorRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ListItemRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.viewholders.AdsViewHolder
import com.kevalpatel2106.yip.dashboard.adapter.viewholders.DeadlineListViewHolder
import com.kevalpatel2106.yip.dashboard.adapter.viewholders.DeadlineViewHolder
import com.kevalpatel2106.yip.dashboard.adapter.viewholders.EmptyViewHolder
import com.kevalpatel2106.yip.dashboard.adapter.viewholders.ErrorViewHolder
import com.kevalpatel2106.yip.dashboard.adapter.viewholders.LoadingViewHolder
import com.kevalpatel2106.yip.dashboard.adapter.viewholders.PaddingViewHolder

internal class DeadlineAdapter(
    private var listener: DeadlineAdapterEventListener
) : ListAdapter<ListItemRepresentable, DeadlineListViewHolder>(DeadlineAdapterDiffCallback) {

    init {
        // We need stable id for the recycler view.
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeadlineListViewHolder {
        return when (viewType) {
            TYPE_LOADING -> LoadingViewHolder.create(parent)
            TYPE_ERROR -> ErrorViewHolder.create(parent)
            TYPE_EMPTY -> EmptyViewHolder.create(parent)
            TYPE_DEADLINE -> DeadlineViewHolder.create(parent, listener)
            TYPE_AD -> AdsViewHolder.create(parent)
            TYPE_PADDING -> PaddingViewHolder.create(parent)
            else -> error("Invalid view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type

    override fun onBindViewHolder(holder: DeadlineListViewHolder, position: Int) {
        val item = getItem(position)
        when (item.type) {
            TYPE_ERROR -> (holder as ErrorViewHolder).bind(item as ErrorRepresentable)
            TYPE_EMPTY -> (holder as EmptyViewHolder).bind(item as EmptyRepresentable)
            TYPE_DEADLINE -> (holder as DeadlineViewHolder).bind(item as DeadlineListItem)
        }
    }

    override fun getItemId(position: Int): Long {
        val item = getItem(position)
        return if (item is DeadlineListItem) {
            item.deadline.id
        } else {
            (item.type * position).toLong()
        }
    }

    companion object {
        internal const val TYPE_LOADING = 3432
        internal const val TYPE_ERROR = 324
        internal const val TYPE_EMPTY = 4533
        internal const val TYPE_DEADLINE = 34533
        internal const val TYPE_AD = 546
        internal const val TYPE_PADDING = 2345
    }
}
