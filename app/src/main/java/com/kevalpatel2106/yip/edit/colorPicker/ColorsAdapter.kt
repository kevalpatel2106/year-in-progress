package com.kevalpatel2106.yip.edit.colorPicker

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.kevalpatel2106.yip.entity.ProgressColor

internal class ColorsAdapter(
    private val context: Context,
    private val colorSelectedListener: ColorPickerListener
) : RecyclerView.Adapter<ColorViewHolder>() {

    internal var isLocked: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val colors by lazy {
        ProgressColor.values().map { ColorStates(it.value, false) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder.create(context, parent)
    }

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position], isLocked) { selected ->
            if (isLocked) {
                colorSelectedListener.onLockedColorClicked()
            } else {
                colorSelectedListener.onColorSelected(selected.color)
                setSelectedColor(selected.color)
            }
        }
    }

    internal fun setSelectedColor(@ColorInt selectedColor: Int) {
        colors.forEach { colorState -> colorState.isSelected = colorState.color == selectedColor }
        notifyDataSetChanged()
    }
}
