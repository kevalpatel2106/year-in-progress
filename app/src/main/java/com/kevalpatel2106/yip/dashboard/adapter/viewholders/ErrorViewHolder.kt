package com.kevalpatel2106.yip.dashboard.adapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ErrorRepresentable
import com.kevalpatel2106.yip.databinding.RowErrorBinding
import kotlinx.android.synthetic.main.row_error.error_list_item_btn

internal class ErrorViewHolder(
    private val binding: RowErrorBinding
) : DeadlineListViewHolder(binding.root) {

    fun bind(errorRepresentable: ErrorRepresentable) {
        binding.errorMessage = errorRepresentable.message
        error_list_item_btn.setOnClickListener { errorRepresentable.retry() }
    }

    companion object {
        fun create(parent: ViewGroup): ErrorViewHolder {
            val binding = DataBindingUtil.inflate<RowErrorBinding>(
                LayoutInflater.from(parent.context),
                R.layout.row_error,
                parent,
                false
            )
            return ErrorViewHolder(binding)
        }
    }
}
