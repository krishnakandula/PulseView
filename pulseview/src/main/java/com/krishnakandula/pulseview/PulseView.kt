package com.krishnakandula.pulseview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.krishnakandula.pulseview.background.Background
import com.krishnakandula.pulseview.background.from
import com.krishnakandula.pulseview.grid.Grid
import com.krishnakandula.pulseview.grid.from
import com.krishnakandula.pulseview.linetab.LineTab
import com.krishnakandula.pulseview.linetab.from

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
    private val lineTab: LineTab = LineTab()
    private val lineTabPaint: Paint = Paint().from(lineTab)

    private var sheet: Sheet = Sheet(grid.horizontalLines, grid.verticalLines)
    private lateinit var animationManager: AnimationManager

    companion object {
        private val LOG_TAG = PulseView::class.simpleName
        private val POINT_RADIUS: Float = 10.toPx()
    }

    init {
        pointPaint.color = Color.BLUE
        pointPaint.style = Paint.Style.STROKE
    }

    fun setData(sheet: Sheet) {
        this.sheet = sheet
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width: Int = Math.max(View.MeasureSpec.getSize(widthMeasureSpec), Background.minWidth.toInt())
        val height: Int = Math.max(View.MeasureSpec.getSize(heightMeasureSpec),
                Background.minHeight.toInt() + LineTab.HEIGHT.toInt())

        //Set Background measurements
        background.rect.left = left
        background.rect.top = top
        background.rect.right = background.manager.getBackgroundRight(right, marginParams().leftMargin, marginParams().rightMargin)
        background.rect.bottom = background.manager.getBackgroundBottom(bottom, marginParams().bottomMargin, marginParams().topMargin, LineTab.HEIGHT.toInt())

        //Set Grid measurements
        grid.rect.left = background.rect.left + Grid.GRID_BACKGROUND_OFFSET.toInt()
        grid.rect.top = background.rect.top + Grid.GRID_BACKGROUND_OFFSET.toInt()
        grid.rect.right = background.rect.right - Grid.GRID_BACKGROUND_OFFSET.toInt()
        grid.rect.bottom = background.rect.bottom - Grid.GRID_BACKGROUND_OFFSET.toInt()

        //Set LineTab measurements
        lineTab.rect.left = background.rect.left
        lineTab.rect.top = background.rect.bottom - LineTab.HEIGHT_OFFSET.toInt()
        lineTab.rect.right = background.rect.right
        lineTab.rect.bottom = lineTab.rect.top + LineTab.HEIGHT.toInt() + LineTab.HEIGHT_OFFSET.toInt()

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            drawLineTab(canvas)
            drawBackground(canvas)
            drawGrid(canvas)
            drawPoints(canvas)
        }
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRoundRect(left.toFloat(),
                top.toFloat(),
                getBackgroundWidth(),
                getBackgroundHeight(),
                background.edgeRadius,
                background.edgeRadius,
                backgroundPaint)
    }

    private fun getBackgroundHeight(): Float = bottom.toFloat() - (marginParams().bottomMargin + marginParams().topMargin) - LineTab.HEIGHT

    private fun getBackgroundWidth(): Float = right.toFloat() - (marginParams().rightMargin + marginParams().leftMargin)

    private fun marginParams(): ViewGroup.MarginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams

    private fun drawGrid(canvas: Canvas) {
        drawHorizontalLines(canvas)
        drawVerticalLines(canvas)
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        val points = FloatArray(4 * grid.horizontalLines)

        val offset: Float = getBackgroundHeight() / grid.horizontalLines.toFloat()
        var currentOffset: Float = offset
        var index = 0
        for (line in 0 until grid.horizontalLines) {
            points[index++] = left.toFloat() + 3.toPx()
            points[index++] = currentOffset
            points[index++] = getBackgroundWidth() - 3.toPx()
            points[index++] = currentOffset
            currentOffset += offset
        }

        canvas.drawLines(points, gridPaint)
    }

    private fun drawVerticalLines(canvas: Canvas) {
        val points = FloatArray(4 * grid.verticalLines)

        val offset: Float = getBackgroundWidth() / grid.verticalLines.toFloat()
        var currentOffset: Float = offset
        var index = 0
        for (line in 0 until grid.verticalLines) {
            points[index++] = currentOffset
            points[index++] = top.toFloat() + 3.toPx()
            points[index++] = currentOffset
            points[index++] = getBackgroundHeight() - 3.toPx()
            currentOffset += offset
        }

        canvas.drawLines(points, gridPaint)
    }

    private fun drawPoints(canvas: Canvas) {
        val vOffset = getBackgroundHeight() / grid.horizontalLines.toFloat()
        val hOffset = getBackgroundWidth() / grid.verticalLines.toFloat()
        for (y in 0 until sheet.taps.size) {
            val row = sheet.taps[y]
            val yPosition = (y * vOffset) + (vOffset / 2)
            (0 until row.size)
                    .filter { sheet.taps[y][it] }
                    .map { (it * hOffset) + (hOffset / 2) }
                    .forEach { xPosition -> canvas.drawCircle(xPosition, yPosition, POINT_RADIUS, pointPaint) }
        }
    }

    private fun drawLineTab(canvas: Canvas) {
        canvas.drawRoundRect(left.toFloat(),
                getBackgroundHeight() - LineTab.HEIGHT_OFFSET,
                getBackgroundWidth(),
                getBackgroundHeight() + LineTab.HEIGHT,
                background.edgeRadius,
                background.edgeRadius,
                lineTabPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean = gestureDetector.onTouchEvent(event)

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            if (e != null && pointInBackgroundBoundary(e.x.toInt(), e.y.toInt())) {
                val indices = sheet.getPointIndices(e.x, e.y, left, top, getBackgroundWidth().toInt(), getBackgroundHeight().toInt())
                when (sheet.checkPointExists(indices.first, indices.second)) {
                    true -> sheet.removePoint(indices.first, indices.second)
                    false -> sheet.addPoint(indices.first, indices.second)
                }

                val offsets = sheet.getPointOffsets(left, top, getBackgroundWidth().toInt(), getBackgroundHeight().toInt())
                invalidate((indices.first * offsets.first).toInt(),
                        (indices.second * offsets.second).toInt(),
                        ((indices.first * offsets.first) + offsets.first).toInt(),
                        ((indices.second * offsets.second) + offsets.second).toInt())
            }
            return true
        }
    })

    private fun pointInBackgroundBoundary(x: Int, y: Int): Boolean {
        return (x > left && x < getBackgroundWidth()) && (y > top && y < getBackgroundHeight())
    }
}
