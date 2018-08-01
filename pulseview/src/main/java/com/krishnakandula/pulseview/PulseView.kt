package com.krishnakandula.pulseview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.krishnakandula.pulseview.background.Background
import com.krishnakandula.pulseview.background.BackgroundDrawManager
import com.krishnakandula.pulseview.grid.Grid
import com.krishnakandula.pulseview.grid.GridDrawManager
import com.krishnakandula.pulseview.point.*

class PulseView(context: Context,
                attrs: AttributeSet?,
                defStyleAttr: Int,
                defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)

    private val typedAttrs: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.PulseView)
    private val backgroundManager = BackgroundDrawManager(Background.from(typedAttrs))
    private val gridManager = GridDrawManager(Grid.from(typedAttrs))
    private val pointGridManager = PointGridDrawManager(PointGrid.from(typedAttrs), this::invalidate)
    private var pulse: Pulse = Pulse(gridManager.grid.verticalLines, gridManager.grid.horizontalLines)
    lateinit var animationsManager: PointAnimationsManager

    companion object {
        private val LOG_TAG = PulseView::class.simpleName
    }

    // This must be called
    fun setData(pulse: Pulse) {
        this.pulse = pulse
        gridManager.grid.horizontalLines = pulse.horizontalLines
        gridManager.grid.verticalLines = pulse.verticalLines
        pointGridManager.pointGrid.horizontalLines = pulse.horizontalLines
        pointGridManager.pointGrid.verticalLines = pulse.verticalLines
        animationsManager = ColumnAnimationsManager(MagnifyAnimator.createAnimators(
                pointGridManager.getNumRows(),
                pointGridManager.getNumCols(),
                drawManager = pointGridManager
        ), pointGridManager, this::useHardwareViewLayer)
        animationsManager.postAnimation = this::postAnimation
        invalidate()
    }

    fun useHardwareViewLayer(useHardware: Boolean) {
        if (useHardware) setLayerType(LAYER_TYPE_HARDWARE, null) else setLayerType(LAYER_TYPE_NONE, null)
    }

    private fun postAnimation(animation: () -> Unit) {
        post { animation() }
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
            pointGridManager.draw(canvas, pulse)
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
            var shouldInvalidate = false
            if (e != null) {
                if (pointGridManager.containsClick(e.x, e.y)) {
                    shouldInvalidate = pointGridManager.onClick(e, pulse)
                }
            }

            if(shouldInvalidate) invalidate()
            return true
        }
    })
}
