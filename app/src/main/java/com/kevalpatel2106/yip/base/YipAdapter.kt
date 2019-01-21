package com.kevalpatel2106.yip.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class YipAdapter : ListAdapter<YipItemRepresentable, YipViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YipViewHolder {
        return when (viewType) {
            TYPE_LOADING -> LoadingViewHolder.create(
                parent
            )
            TYPE_ERROR -> ErrorViewHolder.create(
                parent
            )
            TYPE_EMPTY -> EmptyViewHolder.create(
                parent
            )
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
                if (position == itemCount - 1) shouldLoadNext()
            }
        }
    }

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): YipViewHolder
    abstract fun bindViewHolder(holder: YipViewHolder, item: YipItemRepresentable)
    open fun shouldLoadNext() {
        // Override this function.
    }

    companion object {
        internal const val TYPE_LOADING = 3432
        internal const val TYPE_ERROR = 324
        internal const val TYPE_EMPTY = 4533

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<YipItemRepresentable>() {

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: YipItemRepresentable,
                newItem: YipItemRepresentable
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: YipItemRepresentable, newItem: YipItemRepresentable): Boolean {
                return oldItem == newItem
            }
        }
    }
}