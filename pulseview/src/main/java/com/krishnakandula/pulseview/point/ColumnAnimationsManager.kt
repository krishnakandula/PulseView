package com.krishnakandula.pulseview.point

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.timerTask

class ColumnAnimationsManager<ANIMATOR : PointAnimator> : PointAnimationsManager {

    constructor(drawManager: PointGridDrawManager): super(drawManager)

    private var timer: Timer = Timer()
    private var tasks = Array(
            drawManager.pointGrid.verticalLines + 1)
            { col -> AnimationTask(col) }
    private val pointAnimators: List<ANIMATOR>

    init {
        // Create list of point animators
        pointAnimators = List(getNumOfCols(), { })
    }

    override fun startAnimations(period: Long, delay: Long) {
        var startTime = delay
        (0..drawManager.pointGrid.verticalLines).forEachIndexed { col, _ ->
            timer.schedule(tasks[col].animate(), startTime)
            startTime += period
        }
    }

    override fun stopAllAnimations() {
        timer.cancel()
        timer = Timer()
        tasks.forEach { it.stop() }
    }

    private fun getNumOfCols() = drawManager.pointGrid.verticalLines + 1

    inner class AnimationTask(private val col: Int) {
        private val isAnimating = AtomicBoolean()
        private var task: TimerTask? = null

        fun animate(): TimerTask {
            task = timerTask {
                if (!isAnimating.get()) {
                    isAnimating.set(true)
                    postAnimation { pointAnimator.animate(drawManager) }
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
