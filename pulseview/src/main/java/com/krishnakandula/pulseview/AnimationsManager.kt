package com.krishnakandula.pulseview

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.timerTask

class AnimationsManager(internal var pulse: Pulse,
                        private val animatePoints: (col: Int, onAnimFinished: (col: Int) -> Unit) -> Unit) {

    private val currentCol: AtomicInteger = AtomicInteger()
    private var timer: Timer = Timer()
    private var tasks = Array(pulse.verticalLines + 1, { AnimationTask(it) })

    fun startAnimations(cols: List<Int>, period: Long, delay: Long) {
        var startTime = delay
        cols.forEach { col ->
            timer.schedule(tasks[col].animate(), startTime)
            startTime += period
        }
    }

    fun startAnimationsInRange(startCol: Int, endCol: Int, period: Long, delay: Long) {
        if (startCol > endCol) return
        startAnimations((startCol..endCol).toList(), period, delay)
    }

    fun stop(col: Int) {
        if (col < 0 || col > tasks.lastIndex) return
        tasks[col].stop()
    }

    fun stopAll() {
        timer.cancel()
        timer = Timer()
        tasks.forEach { it.stop() }
    }

    inner class AnimationTask(private val col: Int) {
        private val isAnimating = AtomicBoolean()
        private var task: TimerTask? = null

        fun animate(): TimerTask {
            task = timerTask {
                if (!isAnimating.get()) {
                    isAnimating.set(true)
                    animatePoints(col, { isAnimating.set(false) })
                }
            }
            return task!!
        }

        fun stop() {
            isAnimating.set(false)
            task?.cancel()
        }
    }
}
