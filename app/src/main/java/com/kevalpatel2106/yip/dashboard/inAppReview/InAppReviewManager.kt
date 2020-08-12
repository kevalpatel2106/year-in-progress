package com.kevalpatel2106.yip.dashboard.inAppReview

import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewManager
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.ext.showSnack
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.dashboard.inAppReview.InAppReviewViewState.LaunchReviewFlow
import com.kevalpatel2106.yip.dashboard.inAppReview.InAppReviewViewState.NotifyReviewFailed
import com.kevalpatel2106.yip.dashboard.inAppReview.InAppReviewViewState.ShowConfirmationDialog

internal class InAppReviewManager(
    private val manager: ReviewManager,
    private val activity: FragmentActivity
) : LifecycleObserver {
    private val model: InAppReviewViewModel by activity.viewModels()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun observeViewModelState() {
        model.inAppReviewState.nullSafeObserve(activity) { state ->
            when (state) {
                ShowConfirmationDialog -> showRatingDialog()
                is LaunchReviewFlow -> manager.launchReviewFlow(activity, state.info)
                    .addOnCompleteListener { model.onReviewProcessComplete() }
                NotifyReviewFailed -> activity.showSnack(
                    message = activity.getString(R.string.error_message_in_app_review),
                    actonTitle = R.string.retry,
                    actionListener = { model.onReviewNow() }
                )
            }
        }
    }

    private fun showRatingDialog() {
        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.rate_us_dialog_title)
            .setMessage(R.string.rate_us_dialog_message)
            .setPositiveButton(R.string.rate_us_dialog_positive_btn) { _, _ -> model.onReviewNow() }
            .setNegativeButton(R.string.rate_us_dialog_negative_btn) { _, _ -> model.onReviewLater() }
            .setNeutralButton(R.string.rate_us_dialog_neutral_btn) { _, _ -> model.onReviewNever() }
            .setOnCancelListener { model.onReviewLater() }
            .setCancelable(true)
            .show()
    }

    fun askForReviewIfNeeded() = model.onReviewEventTriggered()
}
