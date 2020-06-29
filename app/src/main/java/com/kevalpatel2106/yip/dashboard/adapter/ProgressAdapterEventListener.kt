package com.kevalpatel2106.yip.dashboard.adapter

import com.kevalpatel2106.yip.entity.Progress

interface ProgressAdapterEventListener {
    fun onProgressClicked(progress: Progress)
}
