package com.kevalpatel2106.yip.base

abstract class YipItemRepresentable(val type: Int)

data class EmptyRepresentable(internal val message: String) : YipItemRepresentable(YipAdapter.TYPE_EMPTY) {
    override fun equals(other: Any?): Boolean = (other as? EmptyRepresentable)?.message == message
    override fun hashCode(): Int = message.hashCode()
}

data class ErrorRepresentable(
        internal val message: String,
        internal val retry: () -> Unit
) : YipItemRepresentable(YipAdapter.TYPE_ERROR) {
    override fun equals(other: Any?): Boolean = (other as? ErrorRepresentable)?.message == message
    override fun hashCode(): Int = message.hashCode()
}

object LoadingRepresentable : YipItemRepresentable(YipAdapter.TYPE_LOADING)
