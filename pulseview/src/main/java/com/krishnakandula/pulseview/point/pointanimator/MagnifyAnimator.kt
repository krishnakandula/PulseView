package com.krishnakandula.pulseview.point.pointanimator

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import com.krishnakandula.pulseview.point.PointGridDrawManager

class MagnifyAnimator(row: Int, col: Int) : PointAnimator(row, col) {

    override fun animate(duration: Long,
                         animatorListener: Animator.AnimatorListener,
                         drawManager: PointGridDrawManager): AnimatorSet {
        val propertyRadius = PropertyValuesHolder.ofFloat(
                POINT_RADIUS_PROPERTY,
                drawManager.pointGrid.radius,
                drawManager.pointGrid.maxRadius)
        val propertyRadiusReverse = PropertyValuesHolder.ofFloat(
                POINT_RADIUS_REVERSE_PROPERTY,
                drawManager.pointGrid.maxRadius,
                drawManager.pointGrid.radius
        )

        val animator = ValueAnimator()
        animator.setValues(propertyRadius)
        animator.duration = duration
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation ->
            drawManager.radii[row][col] = animation.getAnimatedValue(POINT_RADIUS_PROPERTY) as Float
            drawManager.invalidate()
        }
        val animatorReverse = ValueAnimator()
        animatorReverse.setValues(propertyRadiusReverse)
        animatorReverse.duration = duration
        animatorReverse.interpolator = AccelerateDecelerateInterpolator()
        animatorReverse.addUpdateListener { animation ->
            drawManager.radii[row][col] = animation.getAnimatedValue(POINT_RADIUS_REVERSE_PROPERTY) as Float
            drawManager.invalidate()
        }
        animatorReverse.addListener(animatorListener)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animator, animatorReverse)

        return animatorSet
    }

    private val POINT_RADIUS_PROPERTY = "POINT_RADIUS_PROPERTY"
    private val POINT_RADIUS_REVERSE_PROPERTY = "POINT_RADIUS_REVERSE_PROPERTY"

    companion object {
        fun createAnimators(rows: Int,
                            cols: Int): List<List<MagnifyAnimator>> {
            return List(rows) { row -> List(cols) { col -> MagnifyAnimator(row, col) }}
        }
    }
}