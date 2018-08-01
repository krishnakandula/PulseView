package com.krishnakandula.pulseview.point

import android.animation.Animator
import android.animation.AnimatorSet

abstract class PointAnimator(internal val row: Int,
                             internal val col: Int,
                             protected val drawManager: PointGridDrawManager) {

    abstract fun animate(duration: Long, animatorListener: Animator.AnimatorListener): AnimatorSet

}
