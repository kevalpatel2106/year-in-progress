package com.kevalpatel2106.yip.recyclerview.representable

import com.kevalpatel2106.yip.recyclerview.YipAdapter

data class ErrorRepresentable(
        internal val message: String,
        internal val retry: () -> Unit
) : YipItemRepresentable(YipAdapter.TYPE_ERROR) {
    override fun equals(other: Any?): Boolean = (other as? ErrorRepresentable)?.message == message
    override fun hashCode(): Int = message.hashCode()
}