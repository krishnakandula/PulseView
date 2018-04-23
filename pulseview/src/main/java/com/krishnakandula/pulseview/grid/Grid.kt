package com.krishnakandula.pulseview.grid

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.krishnakandula.pulseview.R

internal class Grid(var horizontalLines: Int,
                    var verticalLines: Int,
                    val color: Int) {

    val rect = Rect()
    val paint = Paint().from(this)

    companion object {

        const val DEFAULT_HORIZONTAL_LINES: Int = 9
        const val DEFAULT_VERTICAL_LINES = 8
        private const val DEFAULT_GRID_COLOR = Color.DKGRAY

        fun from(typedAttrs: TypedArray): Grid {
            return Grid(typedAttrs.getInt(R.styleable.PulseView_horizontalLines, DEFAULT_HORIZONTAL_LINES),
                    typedAttrs.getInt(R.styleable.PulseView_verticalLines, DEFAULT_VERTICAL_LINES),
                    typedAttrs.getColor(R.styleable.PulseView_gridColor, DEFAULT_GRID_COLOR))
        }

    }
}

internal fun Paint.from(grid: Grid): Paint {
    this.color = grid.color
    return this
}
