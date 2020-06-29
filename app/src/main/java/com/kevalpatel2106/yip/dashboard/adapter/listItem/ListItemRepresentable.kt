package com.kevalpatel2106.yip.dashboard.adapter.listItem

import android.graphics.drawable.GradientDrawable
import com.kevalpatel2106.yip.dashboard.adapter.ProgressAdapter
import com.kevalpatel2106.yip.entity.Progress

sealed class ListItemRepresentable(val type: Int)

internal object AdsItem : ListItemRepresentable(ProgressAdapter.TYPE_AD)

internal object PaddingItem : ListItemRepresentable(ProgressAdapter.TYPE_PADDING)

internal data class ProgressListItem(
    val progress: Progress,
    val progressString: String,
    val backgroundGradient: GradientDrawable
) : ListItemRepresentable(ProgressAdapter.TYPE_PROGRESS_BAR)

object LoadingRepresentable : ListItemRepresentable(ProgressAdapter.TYPE_LOADING)

data class ErrorRepresentable(
    internal val message: String,
    internal val retry: () -> Unit
) : ListItemRepresentable(ProgressAdapter.TYPE_ERROR) {
    override fun equals(other: Any?): Boolean = (other as? ErrorRepresentable)?.message == message
    override fun hashCode(): Int = message.hashCode()
}

data class EmptyRepresentable(
    internal val message: String
) : ListItemRepresentable(ProgressAdapter.TYPE_EMPTY) {
    override fun equals(other: Any?): Boolean = (other as? EmptyRepresentable)?.message == message
    override fun hashCode(): Int = message.hashCode()
}



