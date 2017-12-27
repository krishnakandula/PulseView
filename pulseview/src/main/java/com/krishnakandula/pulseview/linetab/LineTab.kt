package com.krishnakandula.pulseview.linetab

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.krishnakandula.pulseview.background.Background
import com.krishnakandula.pulseview.util.toPx

internal class LineTab(val edgeRadius: Float,
                       val lineWidth: Float,
                       val tabColor: Int,
                       val lineColor: Int) {

    val rect = Rect()
    val tabPaint = setTabPaint()
    val linePaint = setLinePaint()

    companion object {
        private val DEFAULT_TAB_COLOR = Color.DKGRAY
        private val DEFAULT_LINE_COLOR = Color.MAGENTA
        private val DEFAULT_LINE_WIDTH = 3.toPx()
        private val DEFAULT_EDGE_RADIUS = Background.DEFAULT_EDGE_RADIUS

        val HEIGHT = 0.toPx()
        val HEIGHT_OFFSET = 0.toPx()

        fun from(typedAttrs: TypedArray): LineTab {
            return LineTab(DEFAULT_EDGE_RADIUS,
                    DEFAULT_LINE_WIDTH,
                    DEFAULT_TAB_COLOR,
                    DEFAULT_LINE_COLOR)
        }
    }

    private fun setTabPaint(): Paint {
        val paint = Paint()
        paint.color = tabColor

        return paint
    }

    private fun setLinePaint(): Paint {
        val paint = Paint()
        paint.color = lineColor
        paint.strokeWidth = lineWidth

        return paint
    }
}
