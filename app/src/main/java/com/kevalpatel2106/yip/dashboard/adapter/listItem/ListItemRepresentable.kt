package com.kevalpatel2106.yip.dashboard.adapter.listItem

import android.graphics.drawable.GradientDrawable
import com.kevalpatel2106.yip.dashboard.adapter.DeadlineAdapter
import com.kevalpatel2106.yip.entity.Deadline

sealed class ListItemRepresentable(val type: Int)

internal object AdsItem : ListItemRepresentable(DeadlineAdapter.TYPE_AD)

internal object PaddingItem : ListItemRepresentable(DeadlineAdapter.TYPE_PADDING)

internal data class DeadlineListItem(
    val deadline: Deadline,
    val percentString: String,
    val backgroundGradient: GradientDrawable
) : ListItemRepresentable(DeadlineAdapter.TYPE_DEADLINE)

object LoadingRepresentable : ListItemRepresentable(DeadlineAdapter.TYPE_LOADING)

data class ErrorRepresentable(
    internal val message: String,
    internal val retry: () -> Unit
) : ListItemRepresentable(DeadlineAdapter.TYPE_ERROR) {
    override fun equals(other: Any?): Boolean = (other as? ErrorRepresentable)?.message == message
    override fun hashCode(): Int = message.hashCode()
}

data class EmptyRepresentable(internal val message: String) :
    ListItemRepresentable(DeadlineAdapter.TYPE_EMPTY)

