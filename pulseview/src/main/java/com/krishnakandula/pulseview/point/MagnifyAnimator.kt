package com.krishnakandula.pulseview.point

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator

class MagnifyAnimator : PointAnimator {

    private val animatorSet = AnimatorSet()

    constructor(row: Int,
                col: Int,
                drawManager: PointGridDrawManager) : super(row, col, drawManager)

    override fun animate(animatorListener: Animator.AnimatorListener) {
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
        animator.duration = drawManager.pointGrid.animationDuration.toLong() / 2
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation ->
            drawManager.radii[row][col] = animation.getAnimatedValue(POINT_RADIUS_PROPERTY) as Float
            drawManager.invalidate()
        }

        val animatorReverse = ValueAnimator()
        animatorReverse.setValues(propertyRadiusReverse)
        animatorReverse.duration = drawManager.pointGrid.animationDuration.toLong() / 2
        animatorReverse.interpolator = AccelerateDecelerateInterpolator()
        animatorReverse.addUpdateListener { animation ->
            drawManager.radii[row][col] = animation.getAnimatedValue(POINT_RADIUS_REVERSE_PROPERTY) as Float
            drawManager.invalidate()
        }
        animatorReverse.addListener(animatorListener)
        animatorSet.playSequentially(animator, animatorReverse)
        animatorSet.start()
    }

    private val POINT_RADIUS_PROPERTY = "POINT_RADIUS_PROPERTY"
    private val POINT_RADIUS_REVERSE_PROPERTY = "POINT_RADIUS_REVERSE_PROPERTY"

    companion object {
        fun createAnimators(rows: Int,
                            cols: Int,
                            drawManager: PointGridDrawManager): List<List<MagnifyAnimator>> {
            return List(rows) { row -> List(cols) { col -> MagnifyAnimator(row, col, drawManager) }}
        }
    }
}
