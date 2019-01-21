package com.kevalpatel2106.yip.dashboard.adapter

import com.kevalpatel2106.yip.base.YipItemRepresentable
import com.kevalpatel2106.yip.entity.Progress

data class ProgressListItem(val progress: Progress) : YipItemRepresentable(ProgressAdapter.PROGRESS_BAR_TYPE)
object AdsItem : YipItemRepresentable(ProgressAdapter.ADS_TYPE)