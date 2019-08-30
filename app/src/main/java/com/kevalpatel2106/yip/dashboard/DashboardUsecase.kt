package com.kevalpatel2106.yip.dashboard

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.InterstitialAd
import com.kevalpatel2106.yip.R
import timber.log.Timber

internal fun Context.showRatingDialog(rateNow: () -> Unit) {
    AlertDialog.Builder(this, R.style.AppTheme_Dialog_Alert)
        .setTitle(R.string.rate_us_dialog_title)
        .setMessage(R.string.rate_us_dialog_message)
        .setPositiveButton(R.string.rate_us_dialog_positive_btn) { _, _ -> rateNow() }
        .setNegativeButton(R.string.rate_us_dialog_negative_btn, null)
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
