package com.krishnakandula.pulseview.grid

import android.graphics.Canvas
import com.krishnakandula.pulseview.Sheet
import com.krishnakandula.pulseview.util.toPx

internal class GridDrawManager(val grid: Grid) {

    fun draw(canvas: Canvas) {
        drawVerticalLines(canvas)
        drawHorizontalLines(canvas)
    }

    private fun drawVerticalLines(canvas: Canvas) {
        val points = FloatArray(4 * grid.verticalLines)

        val offset: Float = grid.rect.width() / (grid.verticalLines.toFloat() - 1)
        var currentOffset: Float = 0f
        var index = 0
        for (line in 0 until grid.verticalLines) {
            points[index++] = currentOffset
            points[index++] = grid.rect.top.toFloat()
            points[index++] = currentOffset
            points[index++] = grid.rect.bottom.toFloat()
            currentOffset += offset
        }

        canvas.drawLines(points, grid.paint)
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        val points = FloatArray(4 * grid.horizontalLines)

        val offset: Float = grid.rect.height() / (grid.horizontalLines.toFloat() - 1)
        var currentOffset: Float = offset
        var index = 0
        for (line in 0 until grid.horizontalLines) {
            points[index++] = grid.rect.left.toFloat()
            points[index++] = currentOffset
            points[index++] = grid.rect.right.toFloat()
            points[index++] = currentOffset
            currentOffset += offset
        }

        canvas.drawLines(points, grid.paint)
    }
}
