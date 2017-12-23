package com.krishnakandula.pulseview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class PulseView(context: Context,
                attrs: AttributeSet?,
                defStyleAttr: Int,
                defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)

    private val typedAttrs: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.PulseView)
    private val background: Background = Background.from(typedAttrs)
    private val grid: Grid = Grid.from(typedAttrs)
    private val backgroundPaint: Paint = Paint().from(background)
    private val gridPaint: Paint = Paint().from(grid)

    init {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            drawBackground(canvas)
            drawGrid(canvas)
        }
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRoundRect(left.toFloat(),
                top.toFloat(),
                right.toFloat() - (marginParams().rightMargin + marginParams().leftMargin),
                bottom.toFloat() - (marginParams().bottomMargin + marginParams().topMargin),
                background.edgeRadius,
                background.edgeRadius,
                backgroundPaint)
    }

    private fun marginParams(): ViewGroup.MarginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams

    private fun drawGrid(canvas: Canvas) {
        drawHorizontalLines(canvas)
        drawVerticalLines(canvas)
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        val points = FloatArray(4 * grid.horizontalLines)

        val offset: Float = Math.abs(right - left) / grid.horizontalLines.toFloat()
        var currentOffset: Float = offset
        var index = 0
        for (line in 0 until grid.horizontalLines) {
            points[index++] = currentOffset
            points[index++] = top.toFloat() + 3.toPx()
            points[index++] = currentOffset
            points[index++] = bottom.toFloat() - 10.toPx()
            currentOffset += offset
        }

        canvas.drawLines(points, gridPaint)
    }

    private fun drawVerticalLines(canvas: Canvas) {
        val points = FloatArray(4 * grid.verticalLines)

        val offset: Float = Math.abs(bottom - top) / grid.verticalLines.toFloat()
        var currentOffset: Float = offset
        var index = 0
        for (line in 0 until grid.verticalLines) {
            points[index++] = left.toFloat() + 3.toPx()
            points[index++] = currentOffset
            points[index++] = right.toFloat() - 3.toPx()
            points[index++] = currentOffset
            currentOffset += offset
        }

        canvas.drawLines(points, gridPaint)
    }
}
