package com.kevalpatel2106.yip

import com.flextrade.kfixture.KFixture
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import java.util.Date


fun generateDeadline(
    kFixture: KFixture,
    deadlinePercent: Float = kFixture.range(1f..100f),
    notificationPercents: List<Float> = kFixture()
): Deadline {
    return Deadline(
        id = kFixture(),
        title = kFixture(),
        description = kFixture(),
        color = DeadlineColor.COLOR_YELLOW,
        start = Date(System.currentTimeMillis()),
        end = Date(System.currentTimeMillis() - 1),
        deadlineType = DeadlineType.YEAR_DEADLINE,
        notificationPercent = notificationPercents,
        percent = deadlinePercent
    )
}
