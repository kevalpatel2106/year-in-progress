package com.kevalpatel2106.yip.notifications

import com.kevalpatel2106.yip.entity.Deadline

interface DeadlineNotificationHandler {
    fun notify(deadline: Deadline)
}
