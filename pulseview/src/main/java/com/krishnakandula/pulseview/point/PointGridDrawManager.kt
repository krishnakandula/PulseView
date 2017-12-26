package com.krishnakandula.pulseview.point

import android.graphics.Canvas
import android.view.MotionEvent
import com.krishnakandula.pulseview.Sheet

internal class PointGridDrawManager(val pointGrid: PointGrid) {

    fun draw(canvas: Canvas, sheet: Sheet) {
        val vOffset = pointGrid.rect.height() / (pointGrid.horizontalLines.toFloat() + 1)
        val hOffset = pointGrid.rect.width() / (pointGrid.verticalLines.toFloat() + 1)
        for (y in 0 until sheet.taps.size) {
            val row = sheet.taps[y]
            val yPosition = (y * vOffset) + (vOffset / 2) + pointGrid.rect.top
            (0 until row.size)
                    .filter { sheet.taps[y][it] }
                    .map { (it * hOffset) + (hOffset / 2) + pointGrid.rect.left}
                    .forEach { xPosition -> canvas.drawCircle(xPosition, yPosition, pointGrid.radius, pointGrid.paint) }
        }
    }

    fun onClick(e: MotionEvent, sheet: Sheet, onClickListener: () -> Unit) {
        val indices = getPointIndices(e.x, e.y, sheet)
        when (sheet.checkPointExists(indices.first, indices.second)) {
            true -> sheet.removePoint(indices.first, indices.second)
            false -> sheet.addPoint(indices.first, indices.second)
        }
        onClickListener()
    }

    private fun getPointIndices(x: Float, y: Float, sheet: Sheet): Pair<Int, Int> {
        //Calculate offsets
        val hOffset: Int = pointGrid.rect.width() / (pointGrid.verticalLines + 1)
        val vOffset: Int = pointGrid.rect.height() / (pointGrid.horizontalLines + 1)

        var xIndex = Math.floor(x / hOffset.toDouble()).toInt()
        var yIndex = Math.floor(y / vOffset.toDouble()).toInt()

        //Ensure indices are within bounds
        xIndex = Math.max(Math.min(xIndex, sheet.taps[0].lastIndex), 0)
        yIndex = Math.max(Math.min(yIndex, sheet.taps.lastIndex), 0)

        return Pair(xIndex, yIndex)
    }
}
