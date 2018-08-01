package com.krishnakandula.pulseview.point.pointanimator

import android.animation.Animator
import android.animation.AnimatorSet
import com.krishnakandula.pulseview.point.PointGridDrawManager

abstract class PointAnimator(internal val row: Int,
                             internal val col: Int) {

    abstract fun animate(duration: Long,
                         animatorListener: Animator.AnimatorListener,
                         drawManager: PointGridDrawManager): AnimatorSet

}
