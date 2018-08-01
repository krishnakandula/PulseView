package com.krishnakandula.pulseview.point

import android.animation.Animator
import android.animation.AnimatorSet
import android.util.Log
import com.krishnakandula.pulseview.Pulse
import com.krishnakandula.pulseview.util.SimpleAnimationListener
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.timerTask

class ColumnAnimationsManager : PointAnimationsManager {

    constructor(pointAnimators: List<List<PointAnimator>>) : super(pointAnimators)

    private var animation: AnimatorSet? = null
    private val colIsAnimating: Array<Boolean> by lazy { Array(drawManager.getNumCols()) { false } }

    // Will start all animations
    override fun startAnimations(period: Long, delay: Long, pulse: Pulse) {
        startAnimations(period, delay, pulse, 0, drawManager.getNumCols() - 1)
    }

    fun startAnimations(period: Long, delay: Long, pulse: Pulse, startCol: Int, endCol: Int) {
        stopAllAnimations()

        var start = Math.max(0, Math.min(startCol, drawManager.getNumCols() - 1))
        var end = Math.max(0, Math.min(endCol, drawManager.getNumCols() - 1))
        var reverse = end < start

        if (reverse) {
            val temp = start
            start = end
            end = temp
        }

        var filteredPointAnimators = pointAnimators.flatten().filter {
            it.col in start..end && pulse.taps[it.col][it.row]
        }

        val animatorSets = Array(drawManager.getNumCols()) { AnimatorSet() }
        filteredPointAnimators.forEach { animator ->
            if (!colIsAnimating[animator.col]) {
                animatorSets[animator.col].playTogether(animator.animate(period, object : SimpleAnimationListener() {
                    override fun onAnimationEnd(p0: Animator?) {
                        colIsAnimating[animator.col] = false
                    }

                    override fun onAnimationStart(p0: Animator?) {
                        colIsAnimating[animator.col] = true
                    }
                }, drawManager))
            }
        }
        animation = AnimatorSet()
        if (reverse) animatorSets.reverse()
        animation?.playSequentially(*animatorSets)
        animation?.startDelay = delay
        animation?.addListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(p0: Animator?) {
                useHardwareViewLayer(false)
            }

            override fun onAnimationStart(p0: Animator?) {
                useHardwareViewLayer(true)
            }
        })
        animation?.start()
    }

    override fun stopAllAnimations() {
        animation?.end()
        animation?.cancel()
    }

    companion object {
        private val LOG_TAG = ColumnAnimationsManager::class.java.simpleName
    }
}
