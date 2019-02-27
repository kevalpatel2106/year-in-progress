package com.kevalpatel2106.yip.detail

import android.text.SpannableString
import androidx.annotation.ColorInt

data class DetailViewState(
        val isProgressComplete: Boolean,
        val progressPercent: Int,

        val progressTitleText: String,
        val progressPercentText: String,
        val progressStartTimeText: String,
        val progressEndTimeText: String,
        @ColorInt val progressColor: Int,
        val progressTimeLeftText: SpannableString
)