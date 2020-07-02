package com.kevalpatel2106.yip.dashboard.adapter.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.kevalpatel2106.yip.R
import kotlinx.android.synthetic.main.row_list_ads.view.list_ads_view

internal class AdsViewHolder(itemView: View) : DeadlineListViewHolder(itemView) {
    companion object {
        fun create(parent: ViewGroup): AdsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_list_ads, parent, false)
            view.list_ads_view.loadAd(AdRequest.Builder().build())
            return AdsViewHolder(view)
        }
    }
}
