package com.kevalpatel2106.yip.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.view.children
import com.google.android.flexbox.FlexboxLayout
import com.kevalpatel2106.yip.R

class ColorPicker @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    diffStyleAttributes: Int = 0
) : FlexboxLayout(context, attributes, diffStyleAttributes) {
    private var isLockTouchEvent = false
    private val radius by lazy { context.resources.getDimension(R.dimen.spacing_small) }
    private val strokeWidth by lazy { context.resources.getDimension(R.dimen.spacing_pico) }

    var colorSelectedListener: ColorPickerListener? = null

    fun setColors(colors: IntArray) {
        removeAllViews()
        colors.forEach { color ->
            addView(ColorDrawable(context).apply {
                circleColor = color
                circleRadius = radius
                circleStrokeWidth = strokeWidth
                setOnClickListener {
                    colorSelectedListener?.onColorSelected(color)
                    this.isSelected = true
                }
            })
        }
    }

    fun setSelectedColor(selectedColor: Int) {
        children.forEach {
            (it as? ColorDrawable)?.apply { isSelected = circleColor == selectedColor }
        }
    }

    fun lock(isLock: Boolean) {
        children.forEach { (it as? ColorDrawable)?.apply { lock = isLock } }
        isLockTouchEvent = isLock
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (isLockTouchEvent) {
            colorSelectedListener?.onLockedColorClicked()
        }
        return isLockTouchEvent
    }

    interface ColorPickerListener {
        fun onColorSelected(color: Int)
        fun onLockedColorClicked()
    }
}
