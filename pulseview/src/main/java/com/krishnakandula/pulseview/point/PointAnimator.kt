package com.krishnakandula.pulseview.point

import android.animation.Animator
import android.animation.AnimatorSet

abstract class PointAnimator(internal val row: Int,
                             internal val col: Int) {

    abstract fun animate(duration: Long,
                         animatorListener: Animator.AnimatorListener,
                         drawManager: PointGridDrawManager): AnimatorSet

}
