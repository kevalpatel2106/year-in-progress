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
        val showTimeLeft: Boolean,

        val showShareProgress: Boolean
)