package com.kevalpatel2106.yip.core.recyclerview.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class YipViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer
