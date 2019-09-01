package com.kevalpatel2106.yip.dashboard.adapter

import android.graphics.drawable.GradientDrawable
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.recyclerview.representable.YipItemRepresentable

internal data class ProgressListItem(
    val progress: Progress,
    val progressString: String,
    val backgroundGradient: GradientDrawable
) : YipItemRepresentable(ProgressAdapter.PROGRESS_BAR_TYPE)

internal object AdsItem : YipItemRepresentable(ProgressAdapter.ADS_TYPE)
internal object PaddingItem : YipItemRepresentable(ProgressAdapter.PADDING_TYPE)
