package com.krishnakandula.pulseview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class PulseView(context: Context,
                attrs: AttributeSet?,
                defStyleAttr: Int,
                defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)

    private val paint: Paint = Paint()

    init {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)

    }

    override fun onDraw(canvas: Canvas?) {
        val radius = 100.0f
        canvas?.drawCircle(left.toFloat() + radius, top.toFloat() + radius, 100.0f, paint)
    }
}
