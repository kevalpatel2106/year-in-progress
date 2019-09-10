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
    val progressTitleText: String,
    val progressPercent: Int,
    val progressPercentText: String,
    val progressStartTimeText: String,
    val progressEndTimeText: String,

    val progressTimeLeftText: SpannableString,

    val isDeletingProgress: Boolean,
    val cardBackground: GradientDrawable?,
    @ColorInt val progressColor: Int,
    val detailFlipperPosition: ProgressFlipper
) {
    companion object {

        fun initialState(context: Context): DetailViewState {
            return DetailViewState(
                progressEndTimeText = emptyString(),
                cardBackground = context.getBackgroundGradient(Color.GRAY),
                detailFlipperPosition = ProgressFlipper.POS_TIME_LEFT,
                isDeletingProgress = false,
                progressPercent = 0,
                progressPercentText = emptyString(),
                progressStartTimeText = emptyString(),
                progressTimeLeftText = emptySpannableString(),
                progressColor = Color.GRAY,
                progressTitleText = emptyString()
            )
        }
    }
}

internal enum class ProgressFlipper(val value: Int) {
    POS_TIME_LEFT(value = 0),
    POS_SHARE_PROGRESS(value = 1)
}
