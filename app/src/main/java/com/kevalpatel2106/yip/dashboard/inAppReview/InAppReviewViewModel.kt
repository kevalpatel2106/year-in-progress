package com.kevalpatel2106.yip.dashboard.inAppReview

import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.google.android.play.core.review.ReviewManager
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.livedata.SingleLiveEvent
import com.kevalpatel2106.yip.dashboard.inAppReview.InAppReviewViewState.LaunchReviewFlow
import com.kevalpatel2106.yip.dashboard.inAppReview.InAppReviewViewState.NotifyReviewFailed
import com.kevalpatel2106.yip.dashboard.inAppReview.InAppReviewViewState.ShowConfirmationDialog
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import timber.log.Timber
import kotlin.random.Random

internal class InAppReviewViewModel @ViewModelInject constructor(
    private val manager: ReviewManager,
    private val prefsProvider: SharedPrefsProvider
) : BaseViewModel() {
    private val _inAppReviewState = SingleLiveEvent<InAppReviewViewState>()
    val inAppReviewState: LiveData<InAppReviewViewState> = _inAppReviewState

    private var shouldNeverAskRating: Boolean
        get() = prefsProvider.getBoolFromPreference(PREF_KEY_NEVER_ASK_RATE, false)
        set(value) = prefsProvider.savePreferences(PREF_KEY_NEVER_ASK_RATE, value)

    fun onReviewEventTriggered(randomNumber: Int = Random.nextInt(MAX_RANDOM_NUMBER)) {
        if (randomNumber == RANDOM_NUMBER_FOR_RATING && !shouldNeverAskRating) {
            _inAppReviewState.value = ShowConfirmationDialog
        }
    }

    fun onReviewNow() {
        manager.requestReviewFlow().addOnCompleteListener { task ->
            _inAppReviewState.value = if (task.isSuccessful) {
                LaunchReviewFlow(task.result)
            } else {
                NotifyReviewFailed
            }
        }
    }

    fun onReviewLater() {
        shouldNeverAskRating = false
    }

    fun onReviewNever() {
        shouldNeverAskRating = true
    }

    fun onReviewProcessComplete() {
        Timber.i("Review flow complete")
        shouldNeverAskRating = true
    }

    companion object {
        private const val MAX_RANDOM_NUMBER = 10
        private const val PREF_KEY_NEVER_ASK_RATE = "pref_key_rated"

        @VisibleForTesting
        internal const val RANDOM_NUMBER_FOR_RATING = 1
    }
}
