package com.kevalpatel2106.yip.dashboard.inAppReview

import com.google.android.play.core.review.ReviewInfo

sealed class InAppReviewViewState {
    object ShowConfirmationDialog : InAppReviewViewState()
    data class LaunchReviewFlow(val info: ReviewInfo) : InAppReviewViewState()
    object NotifyReviewFailed : InAppReviewViewState()
}
