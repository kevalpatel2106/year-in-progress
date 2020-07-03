package com.kevalpatel2106.yip.dashboard

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kevalpatel2106.yip.R
import me.saket.inboxrecyclerview.InboxRecyclerView
import me.saket.inboxrecyclerview.dimming.TintPainter
import me.saket.inboxrecyclerview.page.ExpandablePageLayout
import timber.log.Timber

internal fun DashboardActivity.showRatingDialog(rateNow: () -> Unit, neverAsk: () -> Unit) {
    MaterialAlertDialogBuilder(this)
        .setTitle(R.string.rate_us_dialog_title)
        .setMessage(R.string.rate_us_dialog_message)
        .setPositiveButton(R.string.rate_us_dialog_positive_btn) { _, _ -> rateNow() }
        .setNegativeButton(R.string.rate_us_dialog_negative_btn) { _, _ -> neverAsk() }
        .setCancelable(false)
        .show()
}

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
    tintPainter = TintPainter.uncoveredArea(color = Color.WHITE, opacity = 0.65F)
    expandablePage = pageLayout
    (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
}
