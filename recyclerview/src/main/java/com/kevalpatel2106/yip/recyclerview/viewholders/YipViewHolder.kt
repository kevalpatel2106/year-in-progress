package com.kevalpatel2106.yip.recyclerview.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kevalpatel2106.yip.recyclerview.reordering.ItemTouchHelperViewHolder
import kotlinx.android.extensions.LayoutContainer

abstract class YipViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer, ItemTouchHelperViewHolder