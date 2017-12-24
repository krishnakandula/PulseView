package com.krishnakandula.pulseview

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint

internal class Grid(val horizontalLines: Int,
                    val verticalLines: Int,
                    val color: Int) {

    companion object {

        internal const val DEFAULT_HORIZONTAL_LINES: Int = 7
        internal const val DEFAULT_VERTICAL_LINES = DEFAULT_HORIZONTAL_LINES
        private const val DEFAULT_GRID_COLOR = Color.WHITE

        fun from(typedAttrs: TypedArray): Grid {
            return Grid(DEFAULT_HORIZONTAL_LINES,
                        DEFAULT_VERTICAL_LINES,
                        typedAttrs.getColor(R.styleable.PulseView_gridColor, DEFAULT_GRID_COLOR))
        }

    }
}

internal fun Paint.from(grid: Grid): Paint {
    this.color = grid.color
    return this
}
