package com.kevalpatel2106.yip.dashboard.adapter

import android.graphics.drawable.GradientDrawable
import com.kevalpatel2106.yip.core.emptyString
import com.kevalpatel2106.yip.dashboard.adapter.listItem.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.DeadlineListItem
import com.kevalpatel2106.yip.dashboard.adapter.listItem.ListItemRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.LoadingRepresentable
import com.kevalpatel2106.yip.dashboard.adapter.listItem.PaddingItem
import com.kevalpatel2106.yip.entity.Deadline
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(Enclosed::class)
class DeadlineAdapterDiffCallbackTest {

    @RunWith(Parameterized::class)
    internal class AreItemsTheSameTest(
        private val input1: ListItemRepresentable,
        private val input2: ListItemRepresentable,
        private val areItemsSame: Boolean
    ) {

        companion object {
            private val deadline = Deadline(
                id = 8765L,
                title = "Test title",
                color = DeadlineColor.COLOR_YELLOW,
                end = Date(
                    System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                        1,
                        TimeUnit.DAYS
                    )
                ),
                start = Date(System.currentTimeMillis()),
                deadlineType = DeadlineType.YEAR_DEADLINE,
                notificationPercent = arrayListOf(),
                percent = 2f
            )
            private val deadlineItem1 =
                DeadlineListItem(
                    deadline,
                    emptyString(),
                    GradientDrawable()
                )
            private val deadlineItem2 =
                DeadlineListItem(
                    deadline.copy(id = 324L),
                    emptyString(),
                    GradientDrawable()
                )

            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any>> {
                return arrayListOf(
                    arrayOf(deadlineItem1, deadlineItem1.copy(), true),
                    arrayOf(deadlineItem1, deadlineItem2, false),
                    arrayOf(PaddingItem, deadlineItem1, false),
                    arrayOf(LoadingRepresentable, deadlineItem1, false),
                    arrayOf(AdsItem, deadlineItem1, false),
                    arrayOf(AdsItem, LoadingRepresentable, false)
                )
            }
        }

        @Test
        fun whenDeadlineListItemsEquals_checkAreItemsTheSame() {
            assertEquals(
                areItemsSame,
                DeadlineAdapterDiffCallback.areItemsTheSame(input1, input2)
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
            private val deadline = Deadline(
                id = 8765L,
                title = "Test title",
                color = DeadlineColor.COLOR_YELLOW,
                end = Date(
                    System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                        1,
                        TimeUnit.DAYS
                    )
                ),
                start = Date(System.currentTimeMillis()),
                deadlineType = DeadlineType.YEAR_DEADLINE,
                notificationPercent = arrayListOf(),
                percent = 2f
            )
            private val deadlineListItem = DeadlineListItem(
                deadline,
                emptyString(),
                GradientDrawable()
            )
            private val deadlineItemDiffId = DeadlineListItem(
                deadline.copy(id = 324L),
                emptyString(),
                GradientDrawable()
            )
            private val deadlineItemDiffTitle = DeadlineListItem(
                deadline.copy(title = "Test title 1"),
                emptyString(),
                GradientDrawable()
            )
            private val deadlineItemDiffPercent = DeadlineListItem(
                deadline.copy(percent = 55F),
                emptyString(),
                GradientDrawable()
            )
            private val deadlineItemDiffColor = DeadlineListItem(
                deadline.copy(color = DeadlineColor.COLOR_BLUE),
                emptyString(),
                GradientDrawable()
            )

            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any>> {
                return arrayListOf(
                    arrayOf(deadlineListItem, deadlineListItem.copy(), true),
                    arrayOf(deadlineListItem, deadlineItemDiffId, true),
                    arrayOf(deadlineListItem, deadlineItemDiffTitle, false),
                    arrayOf(deadlineListItem, deadlineItemDiffPercent, false),
                    arrayOf(deadlineListItem, deadlineItemDiffColor, false),
                    arrayOf(PaddingItem, deadlineListItem, false),
                    arrayOf(LoadingRepresentable, deadlineListItem, false),
                    arrayOf(AdsItem, deadlineListItem, false),
                    arrayOf(AdsItem, LoadingRepresentable, false)
                )
            }
        }

        @Test
        fun whenDeadlineListItemsEquals_checkAreContentsTheSame() {
            assertEquals(
                areContentsTheSame,
                DeadlineAdapterDiffCallback.areContentsTheSame(input1, input2)
            )
        }
    }
}
