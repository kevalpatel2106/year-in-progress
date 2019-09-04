package com.kevalpatel2106.yip.edit.colorPicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.entity.ProgressColor
import kotlinx.android.extensions.LayoutContainer

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
        return ColorViewHolder.create(context)
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
        colors.forEach { colorState ->
            colorState.isSelected = colorState.color == selectedColor
        }
        notifyDataSetChanged()
    }

    interface ColorPickerListener {
        fun onColorSelected(color: Int)
        fun onLockedColorClicked()
    }
}

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

            setOnClickListener {
                onClick(color)
            }
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

internal data class ColorStates(@ColorInt val color: Int, var isSelected: Boolean)

private class ColorDrawable @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    diffStyleAttributes: Int = 0,
    diffStyleRes: Int = 0
) : View(context, attributes, diffStyleAttributes, diffStyleRes) {
    private val lockImage by lazy { ContextCompat.getDrawable(context, R.drawable.ic_lock) }
    private val selectedImage by lazy { ContextCompat.getDrawable(context, R.drawable.ic_correct) }

    internal var isLocked: Boolean = false
        set(value) {
            field = value
            invalidate()
        }
    internal var circleRadius: Float = DEFAULT_CORNER_RADIUS
        set(value) {
            field = value
            centerIconRadius = circleRadius - circleRadius / 3
            invalidate()
        }
    internal var circleStrokeWidth: Float = DEFAULT_STROKE_WIDTH
        set(value) {
            field = value
            refreshPaint()
            invalidate()
        }
    internal var circleColor: Int = Color.WHITE
        set(value) {
            field = value
            refreshPaint()
            invalidate()
        }

    private var centerIconRadius = 0F
    private val circlePaint = Paint()
    private val strokePaint = Paint()

    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private fun refreshPaint() {
        strokePaint.apply {
            color = Color.BLACK
            strokeWidth = circleStrokeWidth
            style = Paint.Style.STROKE
        }
        circlePaint.apply {
            color = circleColor
            style = Paint.Style.FILL
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        centerX = viewHeight / 2F  // Center of the view
        centerY = viewWidth / 2F   // Center of the view

        this.setMeasuredDimension(viewWidth, viewHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerX, centerY, circleRadius, circlePaint)
        canvas.drawCircle(centerX, centerY, circleRadius, strokePaint)

        if (isLocked) {
            lockImage?.apply {
                setBounds(
                    (centerX - centerIconRadius).toInt(),
                    (centerY - centerIconRadius).toInt(),
                    (centerX + centerIconRadius).toInt(),
                    (centerY + centerIconRadius).toInt()
                )
                draw(canvas)
            }
        } else if (isSelected) {
            selectedImage?.apply {
                setBounds(
                    (centerX - centerIconRadius).toInt(),
                    (centerY - centerIconRadius).toInt(),
                    (centerX + centerIconRadius).toInt(),
                    (centerY + centerIconRadius).toInt()
                )
                draw(canvas)
            }
        }
    }

    companion object {
        private const val DEFAULT_CORNER_RADIUS = 20F
        private const val DEFAULT_STROKE_WIDTH = 4F
    }
}
