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

    constructor(pointAnimators: List<List<PointAnimator>>,
                drawManager: PointGridDrawManager,
                useHardwareViewLayer: (useHardware: Boolean) -> Unit) : super(pointAnimators, drawManager, useHardwareViewLayer)

    private var animation: AnimatorSet? = null
    private val colIsAnimating = Array(drawManager.getNumCols()) { false }

    // Will start all animations
    override fun startAnimations(period: Long, delay: Long, pulse: Pulse) {
        val animatorSets = Array(drawManager.getNumCols()) { AnimatorSet() }
        val filteredPointAnimators = pointAnimators.flatten().filter {
            pulse.taps[it.col][it.row]
        }
        filteredPointAnimators.forEach { animator ->
            if (!colIsAnimating[animator.col]) {
                animatorSets[animator.col].playTogether(animator.animate(period, object : SimpleAnimationListener() {
                    override fun onAnimationEnd(p0: Animator?) {
                        colIsAnimating[animator.col] = false
                    }

                    override fun onAnimationStart(p0: Animator?) {
                        colIsAnimating[animator.col] = true
                    }
                }))
            }
        }
        stopAllAnimations()
        animation = AnimatorSet()
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
    }

    companion object {
        private val LOG_TAG = ColumnAnimationsManager::class.java.simpleName
    }
}
