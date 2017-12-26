package com.krishnakandula.pulseview.point

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.krishnakandula.pulseview.grid.Grid
import com.krishnakandula.pulseview.toPx

internal class PointGrid(val horizontalLines: Int,
                         val verticalLines: Int,
                         val radius: Float,
                         val color: Int) {

    val rect = Rect()
    val paint = setPaint()

    companion object {
        private val DEFAULT_HORIZONTAL_LINES = Grid.DEFAULT_HORIZONTAL_LINES
        private val DEFAULT_VERTICAL_LINES = Grid.DEFAULT_VERTICAL_LINES
        private val DEFAULT_COLOR = Color.MAGENTA
        private val DEFAULT_RADIUS = 3.toPx()
        private val DEFAULT_STYLE = Paint.Style.FILL

        fun from(typedAttrs: TypedArray): PointGrid {
            return PointGrid(DEFAULT_HORIZONTAL_LINES,
                    DEFAULT_VERTICAL_LINES,
                    DEFAULT_RADIUS,
                    DEFAULT_COLOR)
        }
    }

    private fun setPaint(): Paint {
        val paint = Paint()
        paint.color = color
        paint.style = DEFAULT_STYLE

        return paint
    }

}
