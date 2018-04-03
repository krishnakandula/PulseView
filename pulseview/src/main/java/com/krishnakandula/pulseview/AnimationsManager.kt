package com.krishnakandula.pulseview

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.timerTask

class AnimationsManager(internal var pulse: Pulse,
                        private val animatePoints: (col: Int) -> Unit) {

    private val currentCol: AtomicInteger = AtomicInteger()
    private val isAnimating: AtomicBoolean = AtomicBoolean()
    private var timer: Timer = Timer()

    fun startAnimationsWithDelay(cols: List<Int>, period: Long) {
        //TODO: Check cols to see if they're all valid
        if (!isAnimating.get()) {
            currentCol.set(cols[0])
            timer.scheduleAtFixedRate(timerTask {
                if (currentCol.get() > cols.lastIndex) stop()
                else {
                    isAnimating.set(true)
                    animatePoints(cols[currentCol.getAndIncrement()])
                }
            }, 0, period)
        }
    }

    fun startAnimationsInRangeWithDelay(startCol: Int, endCol: Int, period: Long) {
        if (startCol > endCol) return
        if (!isAnimating.get()) {
            currentCol.set(startCol)
            timer.scheduleAtFixedRate(timerTask {
                if (currentCol.get() > endCol) stop()
                else {
                    isAnimating.set(true)
                    animatePoints(currentCol.getAndIncrement())
                }
            }, 0, period)
        }
    }

    @Synchronized
    fun stop() {
        timer.cancel()
        timer = Timer()
        isAnimating.set(false)
    }
}
