package com.krishnakandula.pulseview.background

import android.graphics.Canvas
import com.krishnakandula.pulseview.util.toRectF

internal class BackgroundDrawManager(val background: Background) {

    fun getBackgroundBottom(windowBottom: Int, bottomMargin: Int, topMargin: Int): Int {
        return windowBottom - (bottomMargin + topMargin)
    }

    fun getBackgroundRight(windowRight: Int, leftMargin: Int, rightMargin: Int): Int {
        return windowRight - (rightMargin + leftMargin)
    }

    fun draw(canvas: Canvas) {
        canvas.drawRoundRect(background.rect.toRectF(), background.edgeRadius, background.edgeRadius, background.paint)
    }
}
