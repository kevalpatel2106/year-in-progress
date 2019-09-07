package com.kevalpatel2106.yip.dashboard.adapter.progressType

import android.graphics.drawable.GradientDrawable
import com.kevalpatel2106.yip.core.recyclerview.representable.YipItemRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.ProgressAdapter
import com.kevalpatel2106.yip.entity.Progress

internal data class ProgressListItem(
    val progress: Progress,
    val progressString: String,
    val backgroundGradient: GradientDrawable
) : YipItemRepresentable(ProgressAdapter.PROGRESS_BAR_TYPE)
