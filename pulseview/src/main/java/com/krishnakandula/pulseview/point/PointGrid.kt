package com.krishnakandula.pulseview.point

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.krishnakandula.pulseview.R
import com.krishnakandula.pulseview.grid.Grid
import com.krishnakandula.pulseview.util.toPx

internal class PointGrid(val horizontalLines: Int,
                         val verticalLines: Int,
                         val radius: Float,
                         val maxRadius: Float,
                         val animationDuration: Int,
                         val color: Int) {

    val rect = Rect()
    val paint = setPaint()

    companion object {
        private val DEFAULT_HORIZONTAL_LINES = Grid.DEFAULT_HORIZONTAL_LINES
        private val DEFAULT_VERTICAL_LINES = Grid.DEFAULT_VERTICAL_LINES
        private val DEFAULT_COLOR = Color.MAGENTA
        private val DEFAULT_RADIUS = 3.toPx()
        private val MAX_RADIUS = DEFAULT_RADIUS * 3
        private val DEFAULT_ANIM_DURATION = 500
        private val DEFAULT_STYLE = Paint.Style.FILL

        fun from(typedAttrs: TypedArray): PointGrid {
            return PointGrid(typedAttrs.getInt(R.styleable.PulseView_horizontalLines, DEFAULT_HORIZONTAL_LINES),
                    typedAttrs.getInt(R.styleable.PulseView_verticalLines, DEFAULT_VERTICAL_LINES),
                    typedAttrs.getFloat(R.styleable.PulseView_pointRadius, DEFAULT_RADIUS),
                    typedAttrs.getFloat(R.styleable.PulseView_pointMaxRadius, MAX_RADIUS),
                    typedAttrs.getInt(R.styleable.PulseView_pointAnimationDuration, DEFAULT_ANIM_DURATION),
                    typedAttrs.getColor(R.styleable.PulseView_pointColor, DEFAULT_COLOR))
        }
    }

    private fun setPaint(): Paint {
        val paint = Paint()
        paint.color = color
        paint.style = DEFAULT_STYLE

        return paint
    }

}
