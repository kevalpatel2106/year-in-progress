package com.kevalpatel2106.yip.dashboard.adapter

import android.view.ViewGroup
import com.kevalpatel2106.yip.base.YipAdapter
import com.kevalpatel2106.yip.base.YipItemRepresentable
import com.kevalpatel2106.yip.base.YipViewHolder
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.repo.utils.NtpProvider
import javax.inject.Inject

internal class ProgressAdapter @Inject constructor(private val ntpProvider: NtpProvider) : YipAdapter() {
    internal var clickListener: ((progress: Progress) -> Unit)? = null

    override fun getViewHolder(parent: ViewGroup, viewType: Int): YipViewHolder {
        return when (viewType) {
            PROGRESS_BAR_TYPE -> ProgressViewHolder.create(parent)
            ADS_TYPE -> AdsViewHolder.create(parent)
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun bindViewHolder(holder: YipViewHolder, item: YipItemRepresentable) {
        when (holder) {
            is ProgressViewHolder -> holder.bind((item as ProgressListItem).progress, ntpProvider.now(), clickListener)
        }
    }


    companion object {
        internal const val PROGRESS_BAR_TYPE = 34533
        internal const val ADS_TYPE = 546
    }
}