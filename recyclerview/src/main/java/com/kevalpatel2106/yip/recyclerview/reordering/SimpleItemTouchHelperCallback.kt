package com.kevalpatel2106.yip.recyclerview.reordering

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kevalpatel2106.yip.recyclerview.viewholders.YipViewHolder


/**
 * An implementation of [ItemTouchHelper.Callback] that enables basic drag & drop and
 * swipe-to-dismiss. Drag events are automatically started by an item long-press.<br></br>
 *
 * Expects the `RecyclerView.Adapter` to react to [ ] callbacks and the `RecyclerView.ViewHolder` to implement
 * [ItemTouchHelperViewHolder].
 *
 * @author Paul Burke (ipaulpro)
 */
class SimpleItemTouchHelperCallback(private val draggedListener: ItemDraggedListener) :
    ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean = true

    override fun isItemViewSwipeEnabled(): Boolean = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        (viewHolder as? YipViewHolder)?.let {
            val dragFlags = if (it.isDragSupported()) {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
            } else {
                0
            }
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0)
        }
        return 0
    }

    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        draggedListener.onItemMove(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Do nothing
    }
}