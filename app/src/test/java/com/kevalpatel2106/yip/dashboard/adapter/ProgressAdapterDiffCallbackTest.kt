package com.kevalpatel2106.yip.dashboard.adapter

import android.graphics.drawable.GradientDrawable
import com.kevalpatel2106.yip.core.emptyString
import com.kevalpatel2106.yip.dashboard.adapter.listItem.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ListItemRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.LoadingRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.PaddingItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ProgressListItem
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(Enclosed::class)
class ProgressAdapterDiffCallbackTest {

    @RunWith(Parameterized::class)
    internal class AreItemsTheSameTest(
        private val input1: ListItemRepresentable,
        private val input2: ListItemRepresentable,
        private val areItemsSame: Boolean
    ) {

        companion object {
            private val progress = Progress(
                id = 8765L,
                title = "Test title",
                color = ProgressColor.COLOR_YELLOW,
                end = Date(
                    System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                        1,
                        TimeUnit.DAYS
                    )
                ),
                start = Date(System.currentTimeMillis()),
                progressType = ProgressType.YEAR_PROGRESS,
                notificationPercent = arrayListOf(),
                percent = 2f
            )
            private val progressItem1 =
                ProgressListItem(
                    progress,
                    emptyString(),
                    GradientDrawable()
                )
            private val progressItem2 =
                ProgressListItem(
                    progress.copy(id = 324L),
                    emptyString(),
                    GradientDrawable()
                )

            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any>> {
                return arrayListOf(
                    arrayOf(progressItem1, progressItem1.copy(), true),
                    arrayOf(progressItem1, progressItem2, false),
                    arrayOf(PaddingItem, progressItem1, false),
                    arrayOf(LoadingRepresentable, progressItem1, false),
                    arrayOf(AdsItem, progressItem1, false),
                    arrayOf(AdsItem, LoadingRepresentable, false)
                )
            }
        }

        @Test
        fun whenProgressListItemsEquals_checkAreItemsTheSame() {
            assertEquals(
                areItemsSame,
                ProgressAdapterDiffCallback.areItemsTheSame(input1, input2)
            )
        }
    }

    @RunWith(Parameterized::class)
    internal class AreContentsTheSameTest(
        private val input1: ListItemRepresentable,
        private val input2: ListItemRepresentable,
        private val areContentsTheSame: Boolean
    ) {

        companion object {
            private val progress = Progress(
                id = 8765L,
                title = "Test title",
                color = ProgressColor.COLOR_YELLOW,
                end = Date(
                    System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                        1,
                        TimeUnit.DAYS
                    )
                ),
                start = Date(System.currentTimeMillis()),
                progressType = ProgressType.YEAR_PROGRESS,
                notificationPercent = arrayListOf(),
                percent = 2f
            )
            private val progressItem =
                ProgressListItem(
                    progress,
                    emptyString(),
                    GradientDrawable()
                )
            private val progressItemDiffId =
                ProgressListItem(
                    progress.copy(id = 324L),
                    emptyString(),
                    GradientDrawable()
                )
            private val progressItemDiffTitle =
                ProgressListItem(
                    progress.copy(title = "Test title 1"),
                    emptyString(),
                    GradientDrawable()
                )
            private val progressItemDiffPercent =
                ProgressListItem(
                    progress.copy(percent = 55F),
                    emptyString(),
                    GradientDrawable()
                )
            private val progressItemDiffColor =
                ProgressListItem(
                    progress.copy(color = ProgressColor.COLOR_BLUE),
                    emptyString(),
                    GradientDrawable()
                )

            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any>> {
                return arrayListOf(
                    arrayOf(progressItem, progressItem.copy(), true),
                    arrayOf(progressItem, progressItemDiffId, true),
                    arrayOf(progressItem, progressItemDiffTitle, false),
                    arrayOf(progressItem, progressItemDiffPercent, false),
                    arrayOf(progressItem, progressItemDiffColor, false),
                    arrayOf(PaddingItem, progressItem, false),
                    arrayOf(LoadingRepresentable, progressItem, false),
                    arrayOf(AdsItem, progressItem, false),
                    arrayOf(AdsItem, LoadingRepresentable, false)
                )
            }
        }

        @Test
        fun whenProgressListItemsEquals_checkAreContentsTheSame() {
            assertEquals(
                areContentsTheSame,
                ProgressAdapterDiffCallback.areContentsTheSame(input1, input2)
            )
        }
    }
}
