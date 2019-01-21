package com.kevalpatel2106.yip.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevalpatel2106.yip.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_list_empty_list.*
import kotlinx.android.synthetic.main.row_list_error.*

abstract class YipViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

internal class LoadingViewHolder(containerView: View) : YipViewHolder(containerView) {
    companion object {
        fun create(parent: ViewGroup): LoadingViewHolder {
            return LoadingViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.row_list_loading, parent, false)
            )
        }
    }
}

internal class ErrorViewHolder(containerView: View) : YipViewHolder(containerView) {

    fun bind(errorRepresentable: ErrorRepresentable) {
        error_list_item_text.text = errorRepresentable.message
        error_list_item_btn.setOnClickListener { errorRepresentable.retry.invoke() }
    }

    companion object {
        fun create(parent: ViewGroup): ErrorViewHolder {
            return ErrorViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.row_list_error, parent, false)
            )
        }
    }
}

internal class EmptyViewHolder(containerView: View) : YipViewHolder(containerView) {

    fun bind(emptyRepresentable: EmptyRepresentable) {
        empty_list_item_text.text = emptyRepresentable.message
    }

    companion object {
        fun create(parent: ViewGroup): EmptyViewHolder {
            return EmptyViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.row_list_empty_list, parent, false)
            )
        }
    }
}