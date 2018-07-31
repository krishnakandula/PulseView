package com.krishnakandula.pulseview.point

import android.animation.Animator
import android.util.Log
import com.krishnakandula.pulseview.util.AnimationEndListener
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.timerTask

class ColumnAnimationsManager : PointAnimationsManager {

    constructor(pointAnimators: List<List<PointAnimator>>,
                drawManager: PointGridDrawManager) : super(pointAnimators, drawManager)

    private var timer: Timer = Timer()
    private var tasks = Array(
            drawManager.pointGrid.verticalLines + 1)
    { col -> AnimationTask(pointAnimators.flatten().filter { it.col == col }) }

    // Will start all animations
    override fun startAnimations(period: Long, delay: Long) {
        var startTime = delay
        tasks.forEach { animationTask ->
            timer.schedule(animationTask.animate(), startTime)
            startTime += period
        }
    }

    override fun stopAllAnimations() {
        timer.cancel()
        timer = Timer()
        tasks.forEach { it.stop() }
    }

    inner class AnimationTask(private val animators: List<PointAnimator>) {
        private val isAnimating = AtomicBoolean()
        private var task: TimerTask? = null

        fun animate(): TimerTask {
            task = timerTask {
                if (!isAnimating.get()) {
                    isAnimating.set(true)
                    animators.forEach {
                        val animatorListener = object : AnimationEndListener() {
                            override fun onAnimationEnd(p0: Animator?) {
                                isAnimating.set(false)
                            }
                        }
                        postAnimation { it.animate(animatorListener) }
                    }
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
