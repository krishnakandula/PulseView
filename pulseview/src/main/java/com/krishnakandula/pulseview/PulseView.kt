package com.krishnakandula.pulseview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.krishnakandula.pulseview.background.Background
import com.krishnakandula.pulseview.background.BackgroundDrawManager
import com.krishnakandula.pulseview.grid.Grid
import com.krishnakandula.pulseview.grid.GridDrawManager
import com.krishnakandula.pulseview.point.PointGrid
import com.krishnakandula.pulseview.point.PointGridDrawManager
import com.krishnakandula.pulseview.util.containsExclusive

class PulseView(context: Context,
                attrs: AttributeSet?,
                defStyleAttr: Int,
                defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes), Invalidator {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)

    private val typedAttrs: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.PulseView)
    private val backgroundManager = BackgroundDrawManager(Background.from(typedAttrs))
    private val gridManager = GridDrawManager(Grid.from(typedAttrs))
    private val pointGridManager = PointGridDrawManager(PointGrid.from(typedAttrs), this)

    private var sheet: Sheet = Sheet(gridManager.grid.horizontalLines, gridManager.grid.verticalLines)

    companion object {
        private val LOG_TAG = PulseView::class.simpleName
    }

    fun setData(sheet: Sheet) {
        this.sheet = sheet
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width: Int = View.MeasureSpec.getSize(widthMeasureSpec)
        val height: Int = View.MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            setBackgroundMeasurements()
            setGridMeasurements()
            setPointGridMeasurements()

            backgroundManager.draw(canvas)
            gridManager.draw(canvas)
            pointGridManager.draw(canvas, sheet)
        }
    }

    private fun setBackgroundMeasurements() {
        backgroundManager.background.rect.left = left
        backgroundManager.background.rect.top = top
        backgroundManager.background.rect.right = backgroundManager.getBackgroundRight(right, marginParams().leftMargin, marginParams().rightMargin)
        backgroundManager.background.rect.bottom = backgroundManager.getBackgroundBottom(bottom, marginParams().bottomMargin, marginParams().topMargin)
    }

    private fun setGridMeasurements() {
        gridManager.grid.rect.left = backgroundManager.background.rect.left
        gridManager.grid.rect.top = backgroundManager.background.rect.top
        gridManager.grid.rect.right = backgroundManager.background.rect.right
        gridManager.grid.rect.bottom = backgroundManager.background.rect.bottom
    }

    private fun setPointGridMeasurements() {
        pointGridManager.pointGrid.rect.left = backgroundManager.background.rect.left
        pointGridManager.pointGrid.rect.top = backgroundManager.background.rect.top
        pointGridManager.pointGrid.rect.right = backgroundManager.background.rect.right
        pointGridManager.pointGrid.rect.bottom = backgroundManager.background.rect.bottom
    }

    private fun marginParams(): ViewGroup.MarginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams

    override fun onTouchEvent(event: MotionEvent?): Boolean = gestureDetector.onTouchEvent(event)

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            if (e != null && pointGridManager.pointGrid.rect.containsExclusive(e.x, e.y)) {
                pointGridManager.onClick(e, sheet, { invalidate() })
            }
            return true
        }
    })

    fun startAnimation(row: Int) {
        post { pointGridManager.startAnimation(row) }
    }

    override fun onInvalidate() {
        invalidate()
    }
}
