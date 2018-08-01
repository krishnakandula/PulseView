package com.krishnakandula.pulseview.util

import android.animation.Animator

abstract class SimpleAnimationListener : Animator.AnimatorListener {
    override fun onAnimationRepeat(p0: Animator?) {
        // Do nothing
    }

    override fun onAnimationCancel(p0: Animator?) {
        // Do nothing
    }
}
