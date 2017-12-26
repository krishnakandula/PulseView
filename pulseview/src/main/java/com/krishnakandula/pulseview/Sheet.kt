package com.krishnakandula.pulseview

import com.krishnakandula.pulseview.grid.Grid

data class Sheet(val horizontalLines: Int = Grid.DEFAULT_HORIZONTAL_LINES,
                 val verticalLines: Int = Grid.DEFAULT_VERTICAL_LINES) {

    val taps: List<MutableList<Boolean>> = List(horizontalLines + 1, { MutableList(verticalLines + 1, { false }) })

    fun checkPointExists(x: Int, y: Int): Boolean = taps[y][x]

    fun addPoint(x: Int, y: Int) {
        taps[y][x] = true
    }

    fun removePoint(x: Int, y: Int) {
        taps[y][x] = false
    }

    internal fun getPointIndices(x: Float, y: Float, left: Int, top: Int, right: Int, bottom: Int): Pair<Int, Int> {

        val offsets = getPointOffsets(left, top, right, bottom)
        val xIndex = Math.floor(x / offsets.first.toDouble()).toInt()
        val yIndex = Math.floor(y / offsets.second.toDouble()).toInt()

        return Pair(xIndex, yIndex)
    }

    internal fun getPointOffsets(left: Int, top: Int, right: Int, bottom: Int): Pair<Float, Float> {

        val hOffset: Float = Math.abs(right - left) / (verticalLines.toFloat() - 1)
        val vOffset = Math.abs(bottom - top) / (horizontalLines.toFloat() - 1)

        return Pair(hOffset, vOffset)
    }
}
