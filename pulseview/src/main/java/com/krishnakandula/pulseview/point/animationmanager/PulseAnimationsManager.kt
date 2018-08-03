package com.krishnakandula.pulseview.point.animationmanager

import android.animation.Animator
import android.animation.AnimatorSet
import com.krishnakandula.pulseview.Pulse
import com.krishnakandula.pulseview.point.pointanimator.PointAnimator
import com.krishnakandula.pulseview.util.SimpleAnimationListener

class PulseAnimationsManager(pointAnimators: List<List<PointAnimator>>) : AnimationsManager(pointAnimators) {

    private var animation: AnimatorSet? = null
    private val pointIsAnimating: Array<Array<Boolean>> by lazy {
        Array(drawManager.getNumRows()) { Array(drawManager.getNumCols()) { false } }
    }

    override fun startAnimations(period: Long, delay: Long, pulse: Pulse) {
        val midRow: Int = drawManager.getNumRows() / 2
        val midCol: Int = drawManager.getNumCols() / 2
        startAnimations(period, delay, pulse, midRow, midCol, Math.max(midRow, midCol), false)
    }

    fun startAnimations(period: Long,
                        delay: Long,
                        pulse: Pulse,
                        startRow: Int,
                        startCol: Int,
                        radius: Int,
                        reverse: Boolean) {

        stopAllAnimations()
        if (radius < 0) return

        var adjStartRow = Math.max(0, Math.min(drawManager.getNumRows() - 1, startRow))
        var adjStartCol = Math.max(0, Math.min(drawManager.getNumCols() - 1, startCol))

        val animatorSets = mutableListOf<AnimatorSet>()
        for (r in 0..radius) {
            val radiusPointAnimators = mutableListOf<PointAnimator>()
            // Calculate all points in radius that need to be animated
            if (r == 0) {
                radiusPointAnimators.add(pointAnimators[adjStartRow][adjStartCol])
            } else {

                val topRow = adjStartRow - r
                val leftCol = adjStartCol - r
                val bottomRow = topRow + 2 * r
                val rightCol = leftCol + 2 * r

                // Top/Bottom Row
                for (currentCol in leftCol..rightCol) {
                    if (pointInGrid(topRow, currentCol)) radiusPointAnimators.add(pointAnimators[topRow][currentCol])
                    if (pointInGrid(bottomRow, currentCol)) radiusPointAnimators.add(pointAnimators[bottomRow][currentCol])
                }

                // Left/Right Col
                for (currentRow in topRow + 1 until bottomRow) {
                    if (pointInGrid(currentRow, leftCol)) radiusPointAnimators.add(pointAnimators[currentRow][leftCol])
                    if (pointInGrid(currentRow, rightCol)) radiusPointAnimators.add(pointAnimators[currentRow][rightCol])
                }
            }

            val radiusAnimator = AnimatorSet()
            radiusAnimator.playTogether(radiusPointAnimators.filter {
                pointInGrid(it.row, it.col) && pulse.taps[it.col][it.row]
            }.map {
                it.animate(period, object : SimpleAnimationListener() {
                    override fun onAnimationEnd(p0: Animator?) {
                        pointIsAnimating[it.row][it.col] = false
                    }

                    override fun onAnimationStart(p0: Animator?) {
                        pointIsAnimating[it.row][it.col] = true
                    }
                }, drawManager)
            })
            animatorSets.add(radiusAnimator)
        }

        animation = AnimatorSet()
        if (reverse) animatorSets.reverse()
        animation?.playSequentially(*animatorSets.toTypedArray())
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

    private fun pointInGrid(row: Int, col: Int): Boolean {
        return row in 0 until drawManager.getNumRows() && col in 0 until drawManager.getNumCols()
    }

    override fun stopAllAnimations() {
        animation?.end()
    }

    companion object {
        private val LOG_TAG = PulseAnimationsManager::class.java.simpleName
    }
}
