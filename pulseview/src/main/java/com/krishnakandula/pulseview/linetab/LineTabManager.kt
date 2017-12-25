package com.krishnakandula.pulseview.linetab

import android.graphics.Canvas
import com.krishnakandula.pulseview.toRectF

internal class LineTabManager(val lineTab: LineTab) {

    fun draw(canvas: Canvas) {
        canvas.drawRoundRect(lineTab.rect.toRectF(),
                lineTab.edgeRadius,
                lineTab.edgeRadius,
                lineTab.tabPaint)
    }
}
