package com.krishnakandula.pulseview.util

import android.graphics.Rect
import android.graphics.RectF

fun Rect.toRectF(): RectF = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

fun Rect.containsExclusive(x: Float, y: Float): Boolean = containsExclusive(x.toInt(), y.toInt())

fun Rect.containsExclusive(x: Int, y: Int): Boolean = (x in (left + 1)..(right - 1) && y in (top + 1)..(bottom - 1))
