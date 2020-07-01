package com.kevalpatel2106.yip.repo.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.kevalpatel2106.yip.entity.ProgressType
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import java.util.Date

internal object PrebuiltProcessBuilder {

    @VisibleForTesting
    internal const val DIFF_MILLS_START_AND_END_DATE = 1000L

    internal fun getPrebuiltProgresses(application: Context): List<ProgressDto> {
        return ProgressType.values()
            .filter { it != ProgressType.CUSTOM }
            .map {
                val now = System.currentTimeMillis()
                ProgressDto(
                    title = it.getName(application),
                    color = it.color,
                    progressType = it,
                    start = Date(now),
                    end = Date(now + DIFF_MILLS_START_AND_END_DATE)
                )
            }
    }

    private fun ProgressType.getName(context: Context) = when (this) {
        ProgressType.YEAR_PROGRESS -> context.getString(R.string.this_year)
        ProgressType.DAY_PROGRESS -> context.getString(R.string.today)
        ProgressType.WEEK_PROGRESS -> context.getString(R.string.this_week)
        ProgressType.MONTH_PROGRESS -> context.getString(R.string.this_month)
        ProgressType.QUARTER_PROGRESS -> context.getString(R.string.this_quarter)
        ProgressType.CUSTOM -> throw IllegalStateException("Custom process should not be here.")
    }
}
