package com.kevalpatel2106.yip.detail

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.SpannableString
import androidx.annotation.ColorInt
import com.kevalpatel2106.yip.core.emptySpannableString
import com.kevalpatel2106.yip.core.emptyString
import com.kevalpatel2106.yip.core.getBackgroundGradient

internal data class DetailViewState(
    val titleText: String,
    val percent: Int,
    val deadlinePercentText: String,
    val startTimeText: String,
    val endTimeText: String,

    val timeLeftText: SpannableString,
    val showRepeatable: Boolean,

    val isDeleting: Boolean,
    val cardBackground: GradientDrawable?,
    @ColorInt val deadlineColor: Int,
    val detailFlipperPosition: DetailViewFlipper
) {
    companion object {

        fun initialState(context: Context): DetailViewState {
            return DetailViewState(
                endTimeText = emptyString(),
                cardBackground = context.getBackgroundGradient(Color.GRAY),
                detailFlipperPosition = DetailViewFlipper.POS_TIME_LEFT,
                isDeleting = false,
                percent = 0,
                showRepeatable = false,
                deadlinePercentText = emptyString(),
                startTimeText = emptyString(),
                timeLeftText = emptySpannableString(),
                deadlineColor = Color.GRAY,
                titleText = emptyString()
            )
        }
    }
}

internal enum class DetailViewFlipper(val value: Int) {
    POS_TIME_LEFT(value = 0),
    POS_SHARE(value = 1)
}
