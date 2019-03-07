package com.kevalpatel2106.yip.dashboard.adapter

import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.recyclerview.representable.YipItemRepresentable

data class ProgressListItem(val progress: Progress) : YipItemRepresentable(ProgressAdapter.PROGRESS_BAR_TYPE)
object AdsItem : YipItemRepresentable(ProgressAdapter.ADS_TYPE)
object PaddingItem : YipItemRepresentable(ProgressAdapter.PADDING_TYPE)