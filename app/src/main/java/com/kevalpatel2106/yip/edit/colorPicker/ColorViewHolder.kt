package com.kevalpatel2106.yip.edit.colorPicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevalpatel2106.yip.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_color_picker.color_picker_drawable

internal class ColorViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private val radius by lazy { containerView.context.resources.getDimension(R.dimen.spacing_small) }
    private val strokeWidth by lazy { containerView.context.resources.getDimension(R.dimen.spacing_pico) }

    fun bind(color: ColorStates, isLocked: Boolean, onClick: (color: ColorStates) -> Unit) {
        color_picker_drawable.apply {
            circleColor = color.color
            circleRadius = radius
            circleStrokeWidth = strokeWidth
            this.isLocked = isLocked
            this.isSelected = color.isSelected

            setOnClickListener { onClick(color) }
        }
    }

    companion object {

        fun create(context: Context, parent: ViewGroup): ColorViewHolder {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.row_color_picker, parent, false)
            return ColorViewHolder(view)
        }
    }
}

