package com.kevalpatel2106.yip.dashboard.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class ProgressListViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer
