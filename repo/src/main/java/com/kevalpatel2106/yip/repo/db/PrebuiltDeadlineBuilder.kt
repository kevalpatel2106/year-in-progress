package com.kevalpatel2106.yip.repo.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
import java.util.Date

internal object PrebuiltDeadlineBuilder {

    @VisibleForTesting
    internal const val DIFF_MILLS_START_AND_END_DATE = 1000L

    internal fun getPrebuiltDeadline(application: Context): List<DeadlineDto> {
        return DeadlineType.values()
            .filter { it != DeadlineType.CUSTOM }
            .map {
                val now = System.currentTimeMillis()
                DeadlineDto(
                    title = it.getName(application),
                    color = it.color,
                    type = it,
                    start = Date(now),
                    end = Date(now + DIFF_MILLS_START_AND_END_DATE)
                )
            }
    }

    private fun DeadlineType.getName(context: Context) = when (this) {
        DeadlineType.YEAR_DEADLINE -> context.getString(R.string.this_year)
        DeadlineType.DAY_DEADLINE -> context.getString(R.string.today)
        DeadlineType.WEEK_DEADLINE -> context.getString(R.string.this_week)
        DeadlineType.MONTH_DEADLINE -> context.getString(R.string.this_month)
        DeadlineType.QUARTER_DEADLINE -> context.getString(R.string.this_quarter)
        DeadlineType.CUSTOM -> throw IllegalStateException("Custom process should not be here.")
    }
}
