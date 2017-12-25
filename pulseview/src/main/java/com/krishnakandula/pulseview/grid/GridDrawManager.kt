package com.krishnakandula.pulseview.grid

import android.graphics.Canvas
import com.krishnakandula.pulseview.Sheet
import com.krishnakandula.pulseview.util.toPx

internal class GridDrawManager(val grid: Grid) {

    fun draw(canvas: Canvas, sheet: Sheet) {
        drawVerticalLines(canvas)
        drawHorizontalLines(canvas)
        drawPoints(canvas, sheet)
    }

    private fun drawVerticalLines(canvas: Canvas) {
        val points = FloatArray(4 * grid.verticalLines)

        val offset: Float = grid.rect.width() / grid.verticalLines.toFloat()
        var currentOffset: Float = offset
        var index = 0
        for (line in 0 until grid.verticalLines) {
            points[index++] = currentOffset
            points[index++] = grid.rect.top + 3.toPx()
            points[index++] = currentOffset
            points[index++] = grid.rect.bottom - 3.toPx()
            currentOffset += offset
        }

        canvas.drawLines(points, grid.paint)
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        val points = FloatArray(4 * grid.horizontalLines)

        val offset: Float = grid.rect.height() / grid.horizontalLines.toFloat()
        var currentOffset: Float = offset
        var index = 0
        for (line in 0 until grid.horizontalLines) {
            points[index++] = grid.rect.left + 3.toPx()
            points[index++] = currentOffset
            points[index++] = grid.rect.right - 3.toPx()
            points[index++] = currentOffset
            currentOffset += offset
        }

        canvas.drawLines(points, grid.paint)
    }

    private fun drawPoints(canvas: Canvas, sheet: Sheet) {
        val vOffset = grid.rect.height() / grid.horizontalLines.toFloat()
        val hOffset = grid.rect.width() / grid.verticalLines.toFloat()
        for (y in 0 until sheet.taps.size) {
            val row = sheet.taps[y]
            val yPosition = (y * vOffset) + (vOffset / 2)
            (0 until row.size)
                    .filter { sheet.taps[y][it] }
                    .map { (it * hOffset) + (hOffset / 2) }
                    .forEach { xPosition -> canvas.drawCircle(xPosition, yPosition, grid.pointRadius, grid.paint) }
        }
    }
}
