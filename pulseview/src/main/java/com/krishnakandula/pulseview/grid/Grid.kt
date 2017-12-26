package com.krishnakandula.pulseview.grid

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.krishnakandula.pulseview.R
import com.krishnakandula.pulseview.util.toPx

internal class Grid(val horizontalLines: Int,
                    val verticalLines: Int,
                    val pointRadius: Float,
                    val color: Int) {

    val rect = Rect()
    val paint = Paint().from(this)

    companion object {

        const val DEFAULT_HORIZONTAL_LINES: Int = 9
        const val DEFAULT_VERTICAL_LINES = DEFAULT_HORIZONTAL_LINES
        private const val DEFAULT_GRID_COLOR = Color.CYAN
        private val DEFAULT_POINT_RADIUS = 5.toPx()
        val GRID_BACKGROUND_OFFSET = 3.toPx()

        fun from(typedAttrs: TypedArray): Grid {
            return Grid(DEFAULT_HORIZONTAL_LINES,
                    DEFAULT_VERTICAL_LINES,
                    DEFAULT_POINT_RADIUS,
                    typedAttrs.getColor(R.styleable.PulseView_gridColor, DEFAULT_GRID_COLOR))
        }

    }
}

internal fun Paint.from(grid: Grid): Paint {
    this.color = grid.color
    return this
}
