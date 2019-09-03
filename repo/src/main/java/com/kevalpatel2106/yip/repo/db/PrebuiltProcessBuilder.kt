package com.kevalpatel2106.yip.repo.db

import android.app.Application
import android.content.Context
import com.kevalpatel2106.yip.entity.ProgressType
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import java.util.ArrayList
import java.util.Date

internal object PrebuiltProcessBuilder {

    internal fun getPrebuiltProgresses(application: Application): ArrayList<ProgressDto> {
        val prebuiltProgress = ArrayList<ProgressDto>()
        ProgressType.values().filter {
            it != ProgressType.CUSTOM
        }.forEach {
            prebuiltProgress.add(
                ProgressDto(
                    color = it.color,
                    start = Date(System.currentTimeMillis()),
                    end = Date(System.currentTimeMillis() + 1000),
                    title = it.getName(application),
                    progressType = it
                )
            )
        }
        return prebuiltProgress
    }

    private fun ProgressType.getName(context: Context) = when (this) {
        ProgressType.YEAR_PROGRESS -> context.getString(R.string.this_year)
        ProgressType.DAY_PROGRESS -> context.getString(R.string.today)
        ProgressType.WEEK_PROGRESS -> context.getString(R.string.this_week)
        ProgressType.MONTH_PROGRESS -> context.getString(R.string.this_month)
        ProgressType.QUARTER_PROGRESS -> context.getString(R.string.this_quarter)
        ProgressType.CUSTOM -> "-"
    }
}
