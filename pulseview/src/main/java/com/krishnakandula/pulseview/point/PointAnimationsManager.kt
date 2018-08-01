package com.krishnakandula.pulseview.point

import com.krishnakandula.pulseview.Pulse

abstract class PointAnimationsManager (val pointAnimators: List<List<PointAnimator>>) {

    // Will be set by PulseView
    internal lateinit var postAnimation: (animation: () -> Unit) -> Unit
    internal lateinit var useHardwareViewLayer: (useHardware: Boolean) -> Unit
    internal lateinit var drawManager: PointGridDrawManager

    abstract fun startAnimations(period: Long, delay: Long, pulse: Pulse)

    abstract fun stopAllAnimations()

}
