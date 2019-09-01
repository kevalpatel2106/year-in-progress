package com.kevalpatel2106.yip.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdRequest
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.databinding.RowProgressBinding
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.recyclerview.viewholders.YipViewHolder
import kotlinx.android.synthetic.main.row_list_ads.view.list_ads_view
import kotlinx.android.synthetic.main.row_progress.root_card


internal class ProgressViewHolder(
    private val binding: RowProgressBinding
) : YipViewHolder(binding.root) {

    fun bind(listItem: ProgressListItem, onClick: (progress: Progress) -> Unit) {
        binding.apply {
            progress = listItem.progress
            percentString = listItem.progressString
            percent = listItem.progress.percent.toInt()
        }
        root_card.background = listItem.backgroundGradient
        containerView.setOnClickListener { onClick(listItem.progress) }
    }

    companion object {
        fun create(parent: ViewGroup): ProgressViewHolder {
            val binding = DataBindingUtil.inflate<RowProgressBinding>(
                LayoutInflater.from(parent.context),
                R.layout.row_progress,
                parent,
                false
            )
            return ProgressViewHolder(binding)
        }
    }
}

internal class AdsViewHolder(itemView: View) : YipViewHolder(itemView) {

    companion object {
        fun create(parent: ViewGroup): AdsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_list_ads, parent, false)
            view.list_ads_view.loadAd(AdRequest.Builder().build())// Initialize ads.
            return AdsViewHolder(view)
        }
    }
}

internal class PaddingViewHolder(itemView: View) : YipViewHolder(itemView) {

    companion object {
        fun create(parent: ViewGroup): PaddingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_padding_bottom, parent, false)
            return PaddingViewHolder(view)
        }
    }
}
