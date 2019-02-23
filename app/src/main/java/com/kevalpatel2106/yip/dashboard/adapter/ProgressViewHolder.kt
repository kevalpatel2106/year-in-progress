package com.kevalpatel2106.yip.dashboard.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.setProgressTint
import com.kevalpatel2106.yip.core.showOrHide
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.recyclerview.viewholders.YipViewHolder
import kotlinx.android.synthetic.main.row_list_ads.view.*
import kotlinx.android.synthetic.main.row_progress.*
import java.util.*


internal class ProgressViewHolder(itemView: View) : YipViewHolder(itemView) {

    override fun isDragSupported(): Boolean = true

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

    override fun isDragSupported(): Boolean = false

    companion object {

        fun create(parent: ViewGroup): AdsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_list_ads, parent, false)

            // Set up native ad view
            val adView = (view.findViewById(R.id.ad_view) as UnifiedNativeAdView).apply {
                mediaView = ad_media
                headlineView = ad_headline
                bodyView = ad_body
                callToActionView = ad_call_to_action
                iconView = ad_icon
                priceView = ad_price
                starRatingView = ad_stars
                storeView = ad_store
                advertiserView = ad_advertiser
            }

            AdLoader.Builder(parent.context, "ca-app-pub-3940256099942544/2247696110")
                    .forUnifiedNativeAd { bind(it, adView) }
                    .withNativeAdOptions(NativeAdOptions.Builder().build())
                    .build()
                    .loadAd(AdRequest.Builder().build())

            return AdsViewHolder(view)
        }

        private fun bind(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
            // Some assets are guaranteed to be in every UnifiedNativeAd.
            (adView.headlineView as TextView).text = nativeAd.headline
            (adView.bodyView as TextView).text = nativeAd.body

            adView.callToActionView.showOrHide(nativeAd.callToAction != null)
            (adView.callToActionView as Button).text = nativeAd.callToAction

            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.
            val icon = nativeAd.icon
            adView.iconView.showOrHide(icon != null)
            if (icon != null) {
                (adView.iconView as ImageView).setImageDrawable(icon.drawable)
            }

            adView.priceView.showOrHide(nativeAd.price != null)
            (adView.priceView as TextView).text = nativeAd.price

            adView.storeView.showOrHide(nativeAd.store != null)
            (adView.storeView as TextView).text = nativeAd.store

            adView.starRatingView.showOrHide(nativeAd.starRating != null)
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating?.toFloat() ?: 0f

            adView.advertiserView.showOrHide(nativeAd.advertiser != null)
            (adView.advertiserView as TextView).text = nativeAd.advertiser

            adView.setNativeAd(nativeAd)
        }
    }
}