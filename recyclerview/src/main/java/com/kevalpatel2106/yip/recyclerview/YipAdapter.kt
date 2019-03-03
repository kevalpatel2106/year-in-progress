package com.kevalpatel2106.yip.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kevalpatel2106.yip.recyclerview.representable.EmptyRepresentable
import com.kevalpatel2106.yip.recyclerview.representable.ErrorRepresentable
import com.kevalpatel2106.yip.recyclerview.representable.YipItemRepresentable
import com.kevalpatel2106.yip.recyclerview.viewholders.EmptyViewHolder
import com.kevalpatel2106.yip.recyclerview.viewholders.ErrorViewHolder
import com.kevalpatel2106.yip.recyclerview.viewholders.LoadingViewHolder
import com.kevalpatel2106.yip.recyclerview.viewholders.YipViewHolder

abstract class YipAdapter(diffUtils: DiffUtil.ItemCallback<YipItemRepresentable>)
    : ListAdapter<YipItemRepresentable, YipViewHolder>(diffUtils) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YipViewHolder {
        return when (viewType) {
            TYPE_LOADING -> LoadingViewHolder.create(parent)
            TYPE_ERROR -> ErrorViewHolder.create(parent)
            TYPE_EMPTY -> EmptyViewHolder.create(parent)
            else -> getViewHolder(parent, viewType)
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type

    override fun onBindViewHolder(holder: YipViewHolder, position: Int) {
        val item = getItem(position)
        when (item.type) {
            TYPE_LOADING -> {
                /* Do nothing */
            }
            TYPE_ERROR -> {
                (holder as ErrorViewHolder).bind(item as ErrorRepresentable)
            }
            TYPE_EMPTY -> {
                (holder as EmptyViewHolder).bind(item as EmptyRepresentable)
            }
            else -> {
                bindViewHolder(holder, item)
            }
        }
    }

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): YipViewHolder
    abstract fun bindViewHolder(holder: YipViewHolder, item: YipItemRepresentable)

    companion object {
        internal const val TYPE_LOADING = 3432
        internal const val TYPE_ERROR = 324
        internal const val TYPE_EMPTY = 4533
    }
}