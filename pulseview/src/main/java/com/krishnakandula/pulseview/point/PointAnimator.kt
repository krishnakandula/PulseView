package com.krishnakandula.pulseview.point

import android.animation.Animator

abstract class PointAnimator(internal val row: Int,
                             internal val col: Int,
                             protected val drawManager: PointGridDrawManager) {

    abstract fun animate(animatorListener: Animator.AnimatorListener)

}
