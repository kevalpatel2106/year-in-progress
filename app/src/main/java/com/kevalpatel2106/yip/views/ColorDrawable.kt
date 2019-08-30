package com.kevalpatel2106.yip.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.darkenColor

@Suppress("MagicNumber")
internal class ColorDrawable @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    diffStyleAttributes: Int = 0,
    diffStyleRes: Int = 0
) : View(context, attributes, diffStyleAttributes, diffStyleRes) {

    private val lockImage by lazy { ContextCompat.getDrawable(context, R.drawable.ic_lock) }
    private val selectedImage by lazy { ContextCompat.getDrawable(context, R.drawable.ic_correct) }

    var lock: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    var circleRadius: Float = 20F
        set(value) {
            field = value
            createPaint()
            centerIconRadius = circleRadius - circleRadius / 3
            invalidate()
        }
    var circleStrokeWidth: Float = 4F
        set(value) {
            field = value
            createPaint()
            invalidate()
        }
    var circleColor: Int = Color.WHITE
        set(value) {
            field = value
            createPaint()
            invalidate()
        }

    private var centerIconRadius = 0F
    private val circlePaint = Paint()
    private val strokePaint = Paint()

    private fun createPaint() {
        strokePaint.apply {
            color = darkenColor(circleColor, 0.7F)
            strokeWidth = circleStrokeWidth
            style = Paint.Style.STROKE
        }
        circlePaint.apply {
            color = circleColor
            style = Paint.Style.FILL
        }
    }

    private var centerX: Float = 0f
    private var centerY: Float = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mViewHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val mViewWidth = View.MeasureSpec.getSize(widthMeasureSpec)

        centerX = mViewHeight / 2F  // Center of the view
        centerY = mViewWidth / 2F   // Center of the view

        this.setMeasuredDimension(mViewWidth, mViewHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerX, centerY, circleRadius, circlePaint)
        canvas.drawCircle(centerX, centerY, circleRadius, strokePaint)

        if (lock) {
            lockImage?.setBounds(
                (centerX - centerIconRadius).toInt(),
                (centerY - centerIconRadius).toInt(),
                (centerX + centerIconRadius).toInt(),
                (centerY + centerIconRadius).toInt()
            )
            lockImage?.draw(canvas)
        } else if (isSelected) {
            selectedImage?.setBounds(
                (centerX - centerIconRadius).toInt(),
                (centerY - centerIconRadius).toInt(),
                (centerX + centerIconRadius).toInt(),
                (centerY + centerIconRadius).toInt()
            )
            selectedImage?.draw(canvas)
        }
    }
}
