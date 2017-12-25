package com.krishnakandula.pulseview

import android.graphics.Rect
import android.graphics.RectF

fun Rect.toRectF(): RectF = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
