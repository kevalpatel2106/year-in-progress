package com.kevalpatel2106.yip.dashboard.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.base.YipViewHolder
import com.kevalpatel2106.yip.core.setProgressTint
import com.kevalpatel2106.yip.entity.Progress
import kotlinx.android.synthetic.main.row_list_ads.view.*
import kotlinx.android.synthetic.main.row_progress.*
import java.util.*


internal class ProgressViewHolder(itemView: View) : YipViewHolder(itemView) {
    fun bind(progress: Progress, now: Date, onClick: ((progress: Progress) -> Unit)?) {
        progress_name_title.text = progress.title

        Color.BLACK
        progress_percent.setTextColor(progress.color.value)
        progress_percent.text =
                String.format(itemView.context.getString(R.string.progress_percentage), progress.percent(now))

        progress_bar.setProgressTint(progress.color.value)
        progress_bar.progress = progress.percent(now).toInt()

        containerView.setOnClickListener { onClick?.invoke(progress) }
    }

    companion object {
        fun create(parent: ViewGroup): ProgressViewHolder {
            return ProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_progress, parent, false))
        }
    }
}

internal class AdsViewHolder(itemView: View) : YipViewHolder(itemView) {
    companion object {
        fun create(parent: ViewGroup): AdsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_list_ads, parent, false)
            view.list_ads_view.loadAd(AdRequest.Builder().build())
            return AdsViewHolder(view)
        }
    }
}