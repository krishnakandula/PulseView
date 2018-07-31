package com.krishnakandula.pulseview.point

abstract class PointAnimationsManager(val pointAnimators:List<List<PointAnimator>>,
                                      val drawManager: PointGridDrawManager) {

    // Will be set by PulseView
    internal lateinit var postAnimation: (animation: () -> Unit) -> Unit

    abstract fun startAnimations(period: Long, delay: Long)

    abstract fun stopAllAnimations()
}
