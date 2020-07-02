package com.kevalpatel2106.yip.dashboard.adapter

import com.kevalpatel2106.yip.entity.Deadline

interface DeadlineAdapterEventListener {
    fun onDeadlineClicked(deadline: Deadline)
}
