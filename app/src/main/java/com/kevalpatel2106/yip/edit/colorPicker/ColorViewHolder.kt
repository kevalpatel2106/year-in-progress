package com.kevalpatel2106.yip.edit.colorPicker

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevalpatel2106.yip.R
import kotlinx.android.extensions.LayoutContainer

internal class ColorViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private val radius by lazy { containerView.context.resources.getDimension(R.dimen.spacing_small) }
    private val strokeWidth by lazy { containerView.context.resources.getDimension(R.dimen.spacing_pico) }

    fun bind(color: ColorStates, isLocked: Boolean, onClick: (color: ColorStates) -> Unit) {
        (containerView as ColorDrawable).apply {
            circleColor = color.color
            circleRadius = radius
            circleStrokeWidth = strokeWidth
            this.isLocked = isLocked
            this.isSelected = color.isSelected

            setOnClickListener { onClick(color) }
        }
    }

    companion object {

        fun create(context: Context): ColorViewHolder {
            return ColorViewHolder(ColorDrawable(context).apply {
                val height = context.resources.getDimensionPixelSize(R.dimen.color_picker_height)
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height)
            })
        }
    }
}

