package com.kevalpatel2106.yip.detail

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.SpannableString
import androidx.annotation.ColorInt
import com.kevalpatel2106.yip.core.ext.emptySpannableString
import com.kevalpatel2106.yip.core.ext.emptyString
import com.kevalpatel2106.yip.core.ext.getBackgroundGradient

internal data class DetailViewState(
    val isDeleting: Boolean,
    val titleText: String,

    val descriptionText: String?,
    val hasDescription: Boolean,

    val percent: Int,
    val percentText: String,

    val startTimeText: String,
    val endTimeText: String,

    val timeLeftText: SpannableString,
    val showRepeatable: Boolean,

    val cardBackground: GradientDrawable?,
    @ColorInt val deadlineColor: Int
) {
    companion object {

        fun initialState(context: Context): DetailViewState {
            return DetailViewState(
                endTimeText = emptyString(),
                cardBackground = context.getBackgroundGradient(Color.GRAY),
                isDeleting = false,
                percent = 0,
                showRepeatable = false,
                percentText = emptyString(),
                startTimeText = emptyString(),
                timeLeftText = emptySpannableString(),
                deadlineColor = Color.GRAY,
                titleText = emptyString(),
                descriptionText = emptyString(),
                hasDescription = false
            )
        }
    }
}
