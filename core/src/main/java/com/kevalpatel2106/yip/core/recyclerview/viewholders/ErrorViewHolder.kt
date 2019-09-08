package com.kevalpatel2106.yip.core.recyclerview.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevalpatel2106.feature.core.R
import com.kevalpatel2106.yip.core.recyclerview.representable.ErrorRepresentable
import kotlinx.android.synthetic.main.row_list_error.error_list_item_btn
import kotlinx.android.synthetic.main.row_list_error.error_list_item_text

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