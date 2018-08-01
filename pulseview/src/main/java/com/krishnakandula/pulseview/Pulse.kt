package com.krishnakandula.pulseview

import com.krishnakandula.pulseview.grid.Grid
import com.krishnakandula.pulseview.point.PointGridDrawManager

data class Pulse(val verticalLines: Int = Grid.DEFAULT_VERTICAL_LINES,
                 val horizontalLines: Int = Grid.DEFAULT_HORIZONTAL_LINES) {

    init {
        if (horizontalLines < 0 || horizontalLines == Int.MAX_VALUE)
            throw IllegalArgumentException("Number of horizontal lines must be between 0 and ${Int.MAX_VALUE - 1}")
        if (verticalLines < 0 || verticalLines == Int.MAX_VALUE)
            throw IllegalArgumentException("Number of vertical lines must be between 0 and ${Int.MAX_VALUE - 1}")
    }


    val numCols: Int = PointGridDrawManager.getNumCols(verticalLines)
    val numRows: Int = PointGridDrawManager.getNumRows(verticalLines)
    val taps: List<MutableList<Boolean>> = List(numCols) { MutableList(numRows) { false } }

    fun checkPointExists(x: Int, y: Int): Boolean = checkValidPoint(x, y) && taps[x][y]

    private fun checkValidPoint(x: Int, y: Int): Boolean = x in 0..taps.lastIndex && y in 0..taps[x].lastIndex

    fun addPoint(x: Int, y: Int) {
        if (checkValidPoint(x, y)) taps[x][y] = true
    }

    fun removePoint(x: Int, y: Int) {
        if (checkValidPoint(x, y)) taps[x][y] = false
    }
}
