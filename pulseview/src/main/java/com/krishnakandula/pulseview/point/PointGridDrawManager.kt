package com.krishnakandula.pulseview.point

import android.graphics.Canvas
import android.view.MotionEvent
import com.krishnakandula.pulseview.Pulse
import com.krishnakandula.pulseview.util.containsExclusive

class PointGridDrawManager(val pointGrid: PointGrid, internal val invalidate: () -> Unit) {

    internal val radii: List<MutableList<Float>> = List(getNumRows()) { MutableList(getNumCols()) { pointGrid.radius } }

    fun containsClick(x: Float, y: Float): Boolean = pointGrid.rect.containsExclusive(x, y)

    fun draw(canvas: Canvas, pulse: Pulse) {
        val vOffset = pointGrid.rect.height() / (pointGrid.horizontalLines.toFloat() + 1)
        val hOffset = pointGrid.rect.width() / (pointGrid.verticalLines.toFloat() + 1)
        for (x in 0 until pulse.taps.size) {
            val col = pulse.taps[x]
            val xPosition = (x * hOffset) + (hOffset / 2) + pointGrid.rect.left
            for (y in 0 until col.size) {
                if (pulse.checkPointExists(x, y)) {
                    val yPosition = (y * vOffset) + (vOffset / 2) + pointGrid.rect.top
                    canvas.drawCircle(xPosition, yPosition, radii[y][x], pointGrid.paints[y])
                }
            }
        }
    }

    fun onClick(e: MotionEvent, pulse: Pulse): Boolean {
        val indices = getPointIndices(e.x, e.y, pulse)
        when (pulse.checkPointExists(indices.first, indices.second)) {
            true -> pulse.removePoint(indices.first, indices.second)
            false -> pulse.addPoint(indices.first, indices.second)
        }
        return true
    }

    fun getNumCols() = pointGrid.verticalLines + 1

    fun getNumRows() = pointGrid.horizontalLines + 1

    private fun getPointIndices(x: Float, y: Float, pulse: Pulse): Pair<Int, Int> {
        //Calculate offsets
        val hOffset: Int = pointGrid.rect.width() / (pointGrid.verticalLines + 1)
        val vOffset: Int = pointGrid.rect.height() / (pointGrid.horizontalLines + 1)

        var xIndex = Math.floor(x / hOffset.toDouble()).toInt()
        var yIndex = Math.floor(y / vOffset.toDouble()).toInt()

        //Ensure indices are within bounds
        xIndex = Math.max(Math.min(xIndex, pulse.taps.lastIndex), 0)
        yIndex = Math.max(Math.min(yIndex, pulse.taps.first().lastIndex), 0)

        return Pair(xIndex, yIndex)
    }
}
