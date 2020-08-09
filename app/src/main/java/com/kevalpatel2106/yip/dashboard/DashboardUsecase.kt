package com.kevalpatel2106.yip.dashboard

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.InterstitialAd
import com.kevalpatel2106.yip.R
import me.saket.inboxrecyclerview.InboxRecyclerView
import me.saket.inboxrecyclerview.dimming.TintPainter
import me.saket.inboxrecyclerview.page.ExpandablePageLayout
import timber.log.Timber

internal fun Context.prepareInterstitialAd(): InterstitialAd {
    return InterstitialAd(this).apply {
        adUnitId = getString(R.string.detail_interstitial_ad_id)
        adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                show()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                Timber.i("The interstitial ad loading failed.")
            }
        }
    }
}

internal fun InboxRecyclerView.setUp(pageLayout: ExpandablePageLayout) {
    tintPainter = TintPainter.uncoveredArea(color = Color.WHITE, opacity = 0.05F)
    expandablePage = pageLayout
    (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
}
