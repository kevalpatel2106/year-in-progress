package com.kevalpatel2106.yip.recyclerview

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kevalpatel2106.yip.recyclerview.reordering.ItemDraggedListener
import com.kevalpatel2106.yip.recyclerview.reordering.SimpleItemTouchHelperCallback

fun RecyclerView.enabledReordering(listener: ItemDraggedListener) {
    ItemTouchHelper(SimpleItemTouchHelperCallback(listener)).attachToRecyclerView(this)
}