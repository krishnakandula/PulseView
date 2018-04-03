package com.krishnakandula.pulseview

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PulseTest {

    private lateinit var pulse: Pulse
    private val horizontalLines = 10
    private val verticalLines = 10

    @Before
    fun setup() {
        pulse = Pulse(verticalLines, horizontalLines)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInitPulseIllegalArgumentExceptionThrownWhenVerticalLinesBelow0() {
        Pulse(-1, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInitPulseIllegalArgumentExceptionThrownWhenHorizontalLinesBelow0() {
        Pulse(0, -1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInitPulseIllegalArgumentExceptionThrownWhenVerticalLinesIntMaxValue() {
        Pulse(Int.MAX_VALUE, 1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInitPulseIllegalArgumentExceptionThrownWhenHorizontalLinesIntMaxValue() {
        Pulse(0, Int.MAX_VALUE)
    }

    @Test
    fun testAddPointWhenPointOutOfBounds() {
        pulse.addPoint(-1, 0)
        pulse.addPoint(Int.MIN_VALUE, 0)
        pulse.addPoint(0, Int.MAX_VALUE)
        pulse.addPoint(-1, -1)
        pulse.addPoint(-1847, 2380)
        pulse.addPoint(verticalLines + 1, horizontalLines + 1)
        var hasTrue = false
        pulse.taps.forEach {
            it.forEach { point -> hasTrue = hasTrue || point }
        }
        assertFalse(hasTrue)
    }

    @Test
    fun testAddPointWhenValidPoint() {
        pulse.addPoint(0, 0)
        pulse.addPoint(1, 1)
        pulse.addPoint(verticalLines - 1, horizontalLines - 1)
        pulse.addPoint(verticalLines / 2, horizontalLines / 2)
        assertTrue(pulse.taps[0][0])
        assertTrue(pulse.taps[1][1])
        assertTrue(pulse.taps[verticalLines - 1][horizontalLines - 1])
        assertTrue(pulse.taps[verticalLines / 2][horizontalLines / 2])
    }

    @Test
    fun testCheckPointExistsWhenValidPoint() {
        pulse.addPoint(0, 0)
        pulse.addPoint(1, 5)
        pulse.addPoint(7, 7)
        assertTrue(pulse.checkPointExists(0,0))
        assertTrue(pulse.checkPointExists(1,5))
        assertTrue(pulse.checkPointExists(7,7))
        assertFalse(pulse.checkPointExists(8,7))
    }
}
