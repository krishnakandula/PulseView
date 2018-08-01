package com.krishnakandula.pulseview.point

import com.krishnakandula.pulseview.Pulse

abstract class PointAnimationsManager(val pointAnimators:List<List<PointAnimator>>,
                                      val drawManager: PointGridDrawManager,
                                      val useHardwareViewLayer: (useHardware: Boolean) -> Unit) {

    // Will be set by PulseView
    internal lateinit var postAnimation: (animation: () -> Unit) -> Unit

    abstract fun startAnimations(period: Long, delay: Long, pulse: Pulse)

    abstract fun stopAllAnimations()
}
