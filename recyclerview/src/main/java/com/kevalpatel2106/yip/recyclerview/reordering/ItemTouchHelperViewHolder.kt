package com.kevalpatel2106.yip.recyclerview.reordering


/**
 * Interface to notify an item ViewHolder of relevant callbacks from [ ].
 *
 * @author Paul Burke (ipaulpro)
 */
interface ItemTouchHelperViewHolder {
    fun isDragSupported(): Boolean
}