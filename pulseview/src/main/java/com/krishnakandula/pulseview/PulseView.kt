package com.krishnakandula.pulseview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
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
    private val pointPaint: Paint = Paint()

    var sheet: Sheet = Sheet(grid.horizontalLines, grid.verticalLines)

    companion object {
        private val LOG_TAG = PulseView::class.simpleName
        private val POINT_RADIUS: Float = 10.toPx()
    }

    init {
        pointPaint.color = Color.BLUE
        pointPaint.style = Paint.Style.STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width: Int = Math.max(View.MeasureSpec.getSize(widthMeasureSpec), Background.minWidth.toInt())
        val height: Int = Math.max(View.MeasureSpec.getSize(heightMeasureSpec), Background.minHeight.toInt())

        val minHeightWidth = Math.min(width, height)
        setMeasuredDimension(minHeightWidth, minHeightWidth)
    }

    override fun onDraw(canvas: Canvas?) {
        Log.v(LOG_TAG, "onDraw called")
        if (canvas != null) {
            drawBackground(canvas)
            drawGrid(canvas)
            drawPoints(canvas)
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

        val offset: Float = Math.abs(bottom - top) / grid.horizontalLines.toFloat()
        var currentOffset: Float = offset
        var index = 0
        for (line in 0 until grid.horizontalLines) {
            points[index++] = left.toFloat() + 3.toPx()
            points[index++] = currentOffset
            points[index++] = right.toFloat() - 3.toPx()
            points[index++] = currentOffset
            currentOffset += offset
        }

        canvas.drawLines(points, gridPaint)
    }

    private fun drawVerticalLines(canvas: Canvas) {
        val points = FloatArray(4 * grid.verticalLines)

        val offset: Float = Math.abs(right - left) / grid.verticalLines.toFloat()
        var currentOffset: Float = offset
        var index = 0
        for (line in 0 until grid.verticalLines) {
            points[index++] = currentOffset
            points[index++] = top.toFloat() + 3.toPx()
            points[index++] = currentOffset
            points[index++] = bottom.toFloat() - 3.toPx()
            currentOffset += offset
        }

        canvas.drawLines(points, gridPaint)
    }

    private fun drawPoints(canvas: Canvas) {
        val vOffset = Math.abs(bottom - top) / grid.horizontalLines.toFloat()
        val hOffset = Math.abs(right - left) / grid.verticalLines.toFloat()
        for (y in 0 until sheet.taps.size) {
            val row = sheet.taps[y]
            val yPosition = (y * vOffset) + (vOffset / 2)
            (0 until row.size)
                    .filter { sheet.taps[y][it] }
                    .map { (it * hOffset) + (hOffset / 2) }
                    .forEach { xPosition -> canvas.drawCircle(xPosition, yPosition, POINT_RADIUS, pointPaint) }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean = gestureDetector.onTouchEvent(event)

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            if (e != null) {
                val indices = sheet.getPointIndices(e.x, e.y, left, top, right, bottom)
                Log.d(LOG_TAG, "X: ${indices.first}     Y:${indices.second}")
                when (sheet.checkPointExists(indices.first, indices.second)) {
                    true -> sheet.removePoint(indices.first, indices.second)
                    false -> sheet.addPoint(indices.first, indices.second)
                }

                val offsets = sheet.getPointOffsets(left, top, right, bottom)
                invalidate((indices.first * offsets.first).toInt(),
                        (indices.second * offsets.second).toInt(),
                        ((indices.first * offsets.first) + offsets.first).toInt(),
                        ((indices.second * offsets.second) + offsets.second).toInt())
            }
            return true
        }
    })
}
