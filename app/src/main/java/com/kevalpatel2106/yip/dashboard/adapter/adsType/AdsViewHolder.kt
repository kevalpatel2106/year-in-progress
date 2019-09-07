package com.kevalpatel2106.yip.dashboard.adapter.adsType

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.recyclerview.viewholders.YipViewHolder
import kotlinx.android.synthetic.main.row_list_ads.view.list_ads_view


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
