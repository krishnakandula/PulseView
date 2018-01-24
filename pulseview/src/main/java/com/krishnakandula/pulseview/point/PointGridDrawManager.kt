package com.krishnakandula.pulseview.point

import android.animation.AnimatorSet
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import com.krishnakandula.pulseview.Invalidator
import com.krishnakandula.pulseview.Pulse
import com.krishnakandula.pulseview.util.containsExclusive

internal class PointGridDrawManager(val pointGrid: PointGrid, private val invalidator: Invalidator) {

    private val animators: List<AnimatorSet> = List(pointGrid.verticalLines + 1, { AnimatorSet() })
    private val radii: MutableList<Float> = MutableList(pointGrid.verticalLines + 1, { pointGrid.radius })

    companion object {
        private const val POINT_RADIUS_PROPERTY = "POINT_RADIUS_PROPERTY"
        private const val POINT_RADIUS_REVERSE_PROPERTY = "POINT_RADIUS_REVERSE_PROPERTY"

    }

    init {
        setupAnimators()
    }

    fun containsClick(x: Float, y: Float): Boolean = pointGrid.rect.containsExclusive(x, y)

    fun draw(canvas: Canvas, pulse: Pulse) {
        val vOffset = pointGrid.rect.height() / (pointGrid.horizontalLines.toFloat() + 1)
        val hOffset = pointGrid.rect.width() / (pointGrid.verticalLines.toFloat() + 1)
        for (x in 0 until pulse.taps.size) {
            val col = pulse.taps[x]
            val xPosition = (x * hOffset) + (hOffset / 2) + pointGrid.rect.left
            for (y in 0 until col.size) {
                if (pulse.checkPointExists(x, y)) {
                    val yPosition = (y * vOffset) + (vOffset / 2) + pointGrid.rect.top
                    canvas.drawCircle(xPosition, yPosition, radii[x], pointGrid.paint)
                }
            }
        }
    }

    fun startAnimation(col: Int) {
        animators[col].start()
    }

    fun onClick(e: MotionEvent, pulse: Pulse): Boolean {
        val indices = getPointIndices(e.x, e.y, pulse)
        when (pulse.checkPointExists(indices.first, indices.second)) {
            true -> pulse.removePoint(indices.first, indices.second)
            false -> pulse.addPoint(indices.first, indices.second)
        }
        return true
    }

    private fun getPointIndices(x: Float, y: Float, pulse: Pulse): Pair<Int, Int> {
        //Calculate offsets
        val hOffset: Int = pointGrid.rect.width() / (pointGrid.verticalLines + 1)
        val vOffset: Int = pointGrid.rect.height() / (pointGrid.horizontalLines + 1)

        var xIndex = Math.floor(x / hOffset.toDouble()).toInt()
        var yIndex = Math.floor(y / vOffset.toDouble()).toInt()

        //Ensure indices are within bounds
        xIndex = Math.max(Math.min(xIndex, pulse.taps.lastIndex), 0)
        yIndex = Math.max(Math.min(yIndex, pulse.taps.first().lastIndex), 0)

        return Pair(xIndex, yIndex)
    }

    private fun setupAnimators() {
        val propertyRadius = PropertyValuesHolder.ofFloat(POINT_RADIUS_PROPERTY, pointGrid.radius, pointGrid.maxRadius)
        val propertyRadiusReverse = PropertyValuesHolder.ofFloat(POINT_RADIUS_REVERSE_PROPERTY, pointGrid.maxRadius, pointGrid.radius)

        animators.forEachIndexed { index, set ->
            val animator = ValueAnimator()
            animator.setValues(propertyRadius)
            animator.duration = pointGrid.animationDuration.toLong() / 2
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.addUpdateListener { animation ->
                radii[index] = animation.getAnimatedValue(POINT_RADIUS_PROPERTY) as Float
                invalidator.onInvalidate()
            }

            val animatorReverse = ValueAnimator()
            animatorReverse.setValues(propertyRadiusReverse)
            animatorReverse.duration = pointGrid.animationDuration.toLong() / 2
            animatorReverse.interpolator = AccelerateDecelerateInterpolator()
            animatorReverse.addUpdateListener { animation ->
                radii[index] = animation.getAnimatedValue(POINT_RADIUS_REVERSE_PROPERTY) as Float
                invalidator.onInvalidate()
            }
            set.playSequentially(animator, animatorReverse)
        }
    }
}
