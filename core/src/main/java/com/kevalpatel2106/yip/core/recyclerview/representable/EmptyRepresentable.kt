package com.kevalpatel2106.yip.core.recyclerview.representable

import com.kevalpatel2106.yip.core.recyclerview.YipAdapter

data class EmptyRepresentable(
    internal val message: String
) : YipItemRepresentable(YipAdapter.TYPE_EMPTY) {
    override fun equals(other: Any?): Boolean = (other as? EmptyRepresentable)?.message == message
    override fun hashCode(): Int = message.hashCode()
}
