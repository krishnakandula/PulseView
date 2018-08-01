package com.krishnakandula.pulseview.point

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.krishnakandula.pulseview.R
import com.krishnakandula.pulseview.grid.Grid
import com.krishnakandula.pulseview.util.toPx

class PointGrid(var horizontalLines: Int,
                         var verticalLines: Int,
                         val radius: Float,
                         val maxRadius: Float,
                         val primaryColor: Int,
                         val secondaryColor: Int,
                         val useGradient: Boolean) {

    val rect = Rect()
    val paints = calculatePointPaints()

    companion object {
        private val DEFAULT_HORIZONTAL_LINES = Grid.DEFAULT_HORIZONTAL_LINES
        private val DEFAULT_VERTICAL_LINES = Grid.DEFAULT_VERTICAL_LINES
        private val DEFAULT_COLOR = Color.MAGENTA
        private val DEFAULT_SECONDARY_COLOR = Color.BLUE
        private val DEFAULT_RADIUS = 3.toPx()
        private val MAX_RADIUS = DEFAULT_RADIUS * 3
        private val DEFAULT_STYLE = Paint.Style.FILL
        private val DEFAULT_POINT_GRADIENT = true

        fun from(typedAttrs: TypedArray): PointGrid {
            return PointGrid(typedAttrs.getInt(R.styleable.PulseView_horizontalLines, DEFAULT_HORIZONTAL_LINES),
                    typedAttrs.getInt(R.styleable.PulseView_verticalLines, DEFAULT_VERTICAL_LINES),
                    typedAttrs.getDimension(R.styleable.PulseView_pointRadius, DEFAULT_RADIUS),
                    typedAttrs.getDimension(R.styleable.PulseView_pointMaxRadius, MAX_RADIUS),
                    typedAttrs.getColor(R.styleable.PulseView_pointColor, DEFAULT_COLOR),
                    typedAttrs.getColor(R.styleable.PulseView_pointSecondaryColor, DEFAULT_SECONDARY_COLOR),
                    typedAttrs.getBoolean(R.styleable.PulseView_pointGradient, DEFAULT_POINT_GRADIENT))
        }
    }

    private fun calculatePointPaints(): List<Paint> {
        return if (!useGradient) {
            val paint = Paint()
            paint.color = primaryColor
            paint.style = DEFAULT_STYLE
            List(horizontalLines + 1) { paint }
        } else {
            // Calculate gradient
            val startR = Color.red(primaryColor)
            val startG = Color.green(primaryColor)
            val startB = Color.blue(primaryColor)

            val diffR = Color.red(secondaryColor) - startR
            val diffG = Color.green(secondaryColor) - startG
            val diffB = Color.blue(secondaryColor) - startB

            val stepR = diffR / horizontalLines + 1
            val stepG = diffG / horizontalLines + 1
            val stepB = diffB / horizontalLines + 1

            List(horizontalLines + 1) { row ->
                val paint = Paint()
                paint.style = DEFAULT_STYLE
                paint.color = Color.rgb(
                        startR + (row * stepR),
                        startG + (row * stepG),
                        startB + (row * stepB))
                paint
            }
        }
    }
}
