package com.krishnakandula.pulseview.point

import android.graphics.Canvas
import android.view.MotionEvent
import com.krishnakandula.pulseview.Sheet

internal class PointGridDrawManager(val pointGrid: PointGrid) {

    fun draw(canvas: Canvas, sheet: Sheet) {
        val vOffset = pointGrid.rect.height() / pointGrid.horizontalLines.toFloat()
        val hOffset = pointGrid.rect.width() / pointGrid.verticalLines.toFloat()
        for (y in 0 until sheet.taps.size) {
            val row = sheet.taps[y]
            val yPosition = (y * vOffset) + (vOffset / 2)
            (0 until row.size)
                    .filter { sheet.taps[y][it] }
                    .map { (it * hOffset) + (hOffset / 2) }
                    .forEach { xPosition -> canvas.drawCircle(xPosition, yPosition, pointGrid.radius, pointGrid.paint) }
        }
    }

    fun onClick(e: MotionEvent, sheet: Sheet) {
        val indices = getPointIndices(e.x, e.y)
        when (sheet.checkPointExists(indices.first, indices.second)) {
            true -> sheet.removePoint(indices.first, indices.second)
            false -> sheet.addPoint(indices.first, indices.second)
        }
    }

    private fun getPointIndices(x: Float, y: Float): Pair<Int, Int> {
        //Calculate offsets
        val hOffset: Int = pointGrid.rect.width() / pointGrid.verticalLines
        val vOffset: Int = pointGrid.rect.height() / pointGrid.horizontalLines

        val xIndex = Math.floor(x / hOffset.toDouble()).toInt()
        val yIndex = Math.floor(y / vOffset.toDouble()).toInt()

        return Pair(xIndex, yIndex)
    }
}
