package com.kevalpatel2106.yip.dashboard.adapter

import android.view.ViewGroup
import com.kevalpatel2106.yip.core.recyclerview.YipAdapter
import com.kevalpatel2106.yip.core.recyclerview.representable.YipItemRepresentable
import com.kevalpatel2106.yip.core.recyclerview.viewholders.YipViewHolder
import com.kevalpatel2106.yip.dashboard.adapter.adsType.AdsViewHolder
import com.kevalpatel2106.yip.dashboard.adapter.paddingType.PaddingViewHolder
import com.kevalpatel2106.yip.dashboard.adapter.progressType.ProgressListItem
import com.kevalpatel2106.yip.dashboard.adapter.progressType.ProgressViewHolder

internal class ProgressAdapter(
    private var listener: ProgressAdapterEventListener
) : YipAdapter(ProgressAdapterDiffCallback) {

    init {
        // We need stable id for the recycler view.
        setHasStableIds(true)
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): YipViewHolder {
        return when (viewType) {
            PROGRESS_BAR_TYPE -> ProgressViewHolder.create(parent, listener)
            ADS_TYPE -> AdsViewHolder.create(parent)
            PADDING_TYPE -> PaddingViewHolder.create(parent)
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun bindViewHolder(holder: YipViewHolder, item: YipItemRepresentable) {
        when (holder) {
            is ProgressViewHolder -> {
                (item as? ProgressListItem)?.let { holder.bind(it) }
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
    }
}
