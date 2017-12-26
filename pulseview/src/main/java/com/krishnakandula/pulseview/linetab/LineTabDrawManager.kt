package com.krishnakandula.pulseview.linetab

import android.graphics.Canvas
import com.krishnakandula.pulseview.util.toRectF

internal class LineTabDrawManager(val lineTab: LineTab) {

    fun draw(canvas: Canvas) {
        canvas.drawRoundRect(lineTab.rect.toRectF(),
                lineTab.edgeRadius,
                lineTab.edgeRadius,
                lineTab.tabPaint)
    }
}
