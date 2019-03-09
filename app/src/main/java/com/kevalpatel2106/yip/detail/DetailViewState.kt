package com.kevalpatel2106.yip.detail

import android.text.SpannableString
import androidx.annotation.ColorInt

internal data class DetailViewState(
    @ColorInt val backgroundColor: Int,

    val progressTitleText: String,

    val progressPercent: Int,
    val progressPercentText: String,
    @ColorInt val progressPercentTextColor: Int,

    @ColorInt val progressBarColor: Int,

    val progressStartTimeText: String,
    val progressEndTimeText: String,

    val progressTimeLeftText: SpannableString,

    @ColorInt val progressAchievementTextColor: Int,

    val isProgressComplete: ProgressFlipper
)

internal enum class ProgressFlipper(val value: Int) {
    POS_TIME_LEFT(value = 0),
    POS_SHARE_PROGRESS(value = 1)
}