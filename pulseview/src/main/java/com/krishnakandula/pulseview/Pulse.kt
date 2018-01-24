package com.krishnakandula.pulseview

import com.krishnakandula.pulseview.grid.Grid

data class Pulse(val horizontalLines: Int = Grid.DEFAULT_HORIZONTAL_LINES,
                 val verticalLines: Int = Grid.DEFAULT_VERTICAL_LINES) {

    val taps: List<MutableList<Boolean>> = List(verticalLines + 1, { MutableList(horizontalLines + 1, { false }) })

    fun checkPointExists(x: Int, y: Int): Boolean = taps[x][y]

    fun addPoint(x: Int, y: Int) {
        taps[x][y] = true
    }

    fun removePoint(x: Int, y: Int) {
        taps[x][y] = false
    }
}
