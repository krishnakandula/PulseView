package com.krishnakandula.pulseview.background

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

internal class BackgroundDrawManager {

    fun getBackgroundBottom(windowBottom: Int, bottomMargin: Int, topMargin: Int, lineTabHeight: Int): Int {
        return windowBottom - (bottomMargin + topMargin) - lineTabHeight
    }

    fun getBackgroundRight(windowRight: Int, leftMargin: Int, rightMargin: Int): Int {
        return windowRight - (rightMargin + leftMargin)
    }

    fun draw(canvas: Canvas, rect: Rect, paint: Paint) {

    }
}
