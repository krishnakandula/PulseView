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
import com.krishnakandula.pulseview.background.BackgroundDrawManager
import com.krishnakandula.pulseview.grid.Grid
import com.krishnakandula.pulseview.grid.GridDrawManager
import com.krishnakandula.pulseview.linetab.LineTab
import com.krishnakandula.pulseview.linetab.LineTabManager

class PulseView(context: Context,
                attrs: AttributeSet?,
                defStyleAttr: Int,
                defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)

    private val typedAttrs: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.PulseView)
    private val backgroundManager = BackgroundDrawManager(Background.from(typedAttrs))
    private val gridManager = GridDrawManager(Grid.from(typedAttrs))
    private val lineTabManager = LineTabManager(LineTab.from(typedAttrs))

    private val pointPaint: Paint = Paint()

    private var sheet: Sheet = Sheet(gridManager.grid.horizontalLines, gridManager.grid.verticalLines)
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

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            setBackgroundMeasurements()
            backgroundManager.draw(canvas)

            setGridMeasurements()
            gridManager.draw(canvas, sheet)

            setLineTabMeasurements()
            lineTabManager.draw(canvas)
        }
    }

    private fun setBackgroundMeasurements() {
        backgroundManager.background.rect.left = left
        backgroundManager.background.rect.top = top
        backgroundManager.background.rect.right = backgroundManager.getBackgroundRight(right, marginParams().leftMargin, marginParams().rightMargin)
        backgroundManager.background.rect.bottom = backgroundManager.getBackgroundBottom(bottom, marginParams().bottomMargin, marginParams().topMargin, LineTab.HEIGHT.toInt())
    }

    private fun setGridMeasurements() {
        gridManager.grid.rect.left = backgroundManager.background.rect.left + Grid.GRID_BACKGROUND_OFFSET.toInt()
        gridManager.grid.rect.top = backgroundManager.background.rect.top + Grid.GRID_BACKGROUND_OFFSET.toInt()
        gridManager.grid.rect.right = backgroundManager.background.rect.right - Grid.GRID_BACKGROUND_OFFSET.toInt()
        gridManager.grid.rect.bottom = backgroundManager.background.rect.bottom - Grid.GRID_BACKGROUND_OFFSET.toInt()
    }

    private fun setLineTabMeasurements() {
        lineTabManager.lineTab.rect.left = backgroundManager.background.rect.left
        lineTabManager.lineTab.rect.top = backgroundManager.background.rect.bottom - LineTab.HEIGHT_OFFSET.toInt()
        lineTabManager.lineTab.rect.right = backgroundManager.background.rect.right
        lineTabManager.lineTab.rect.bottom = lineTabManager.lineTab.rect.top + LineTab.HEIGHT.toInt() + LineTab.HEIGHT_OFFSET.toInt()
    }

    private fun getBackgroundHeight(): Float = bottom.toFloat() - (marginParams().bottomMargin + marginParams().topMargin) - LineTab.HEIGHT

    private fun getBackgroundWidth(): Float = right.toFloat() - (marginParams().rightMargin + marginParams().leftMargin)

    private fun marginParams(): ViewGroup.MarginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams

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
