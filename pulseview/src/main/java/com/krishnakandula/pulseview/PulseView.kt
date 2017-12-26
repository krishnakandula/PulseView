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
import com.krishnakandula.pulseview.linetab.LineTab
import com.krishnakandula.pulseview.linetab.LineTabDrawManager
import com.krishnakandula.pulseview.point.PointGrid
import com.krishnakandula.pulseview.point.PointGridDrawManager

class PulseView(context: Context,
                attrs: AttributeSet?,
                defStyleAttr: Int,
                defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)

    private val typedAttrs: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.PulseView)
    private val backgroundManager = BackgroundDrawManager(Background.from(typedAttrs))
    private val gridManager = GridDrawManager(Grid.from(typedAttrs))
    private val lineTabManager = LineTabDrawManager(LineTab.from(typedAttrs))
    private val pointGridManager = PointGridDrawManager(PointGrid.from(typedAttrs))

    private var sheet: Sheet = Sheet(gridManager.grid.horizontalLines, gridManager.grid.verticalLines)
    private lateinit var animationManager: AnimationManager

    companion object {
        private val LOG_TAG = PulseView::class.simpleName
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
            setLineTabMeasurements()
            setGridMeasurements()
            setPointGridMeasurements()

            lineTabManager.draw(canvas)
            backgroundManager.draw(canvas)
            gridManager.draw(canvas)
            pointGridManager.draw(canvas, sheet)
        }
    }

    private fun setBackgroundMeasurements() {
        backgroundManager.background.rect.left = left
        backgroundManager.background.rect.top = top
        backgroundManager.background.rect.right = backgroundManager.getBackgroundRight(right, marginParams().leftMargin, marginParams().rightMargin)
        backgroundManager.background.rect.bottom = backgroundManager.getBackgroundBottom(bottom, marginParams().bottomMargin, marginParams().topMargin, LineTab.HEIGHT.toInt())
    }

    private fun setGridMeasurements() {
        gridManager.grid.rect.left = backgroundManager.background.rect.left
        gridManager.grid.rect.top = backgroundManager.background.rect.top
        gridManager.grid.rect.right = backgroundManager.background.rect.right
        gridManager.grid.rect.bottom = backgroundManager.background.rect.bottom
    }

    private fun setLineTabMeasurements() {
        lineTabManager.lineTab.rect.left = backgroundManager.background.rect.left
        lineTabManager.lineTab.rect.top = backgroundManager.background.rect.bottom - LineTab.HEIGHT_OFFSET.toInt()
        lineTabManager.lineTab.rect.right = backgroundManager.background.rect.right
        lineTabManager.lineTab.rect.bottom = lineTabManager.lineTab.rect.top + LineTab.HEIGHT.toInt() + LineTab.HEIGHT_OFFSET.toInt()
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
            if (e != null && pointGridManager.pointGrid.rect.contains(e.x.toInt(), e.y.toInt())) {
                pointGridManager.onClick(e, sheet)
            }
            return true
        }
    })

}
