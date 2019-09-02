package com.kevalpatel2106.yip.detail

import android.graphics.drawable.GradientDrawable
import android.text.SpannableString

internal data class DetailViewState(
    val backgroundDrawable: GradientDrawable,

    val progressTitleText: String,

    val progressPercent: Int,
    val progressPercentText: String,

    val progressStartTimeText: String,
    val progressEndTimeText: String,

    val progressTimeLeftText: SpannableString,

    val isProgressComplete: ProgressFlipper
)

internal enum class ProgressFlipper(val value: Int) {
    POS_TIME_LEFT(value = 0),
    POS_SHARE_PROGRESS(value = 1)
}
