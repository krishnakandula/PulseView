package com.krishnakandula.pulseview.background

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.krishnakandula.pulseview.R
import com.krishnakandula.pulseview.util.toPx

internal class Background(val edgeRadius: Float,
                          val edgeWidth: Float,
                          val color: Int,
                          val style: Paint.Style) {

    val rect = Rect()
    val paint = Paint().from(this)

    companion object {
        private val DEFAULT_EDGE_RADIUS: Float = 16.toPx()
        private val DEFAULT_EDGE_WIDTH: Float = 5.toPx()
        private val DEFAULT_BACKGROUND_COLOR: Int = Color.parseColor("#e6e6e6")

        fun from(typedAttrs: TypedArray): Background {
            return Background(typedAttrs.getFloat(R.styleable.PulseView_backgroundEdgeRadius, DEFAULT_EDGE_RADIUS),
                    typedAttrs.getFloat(R.styleable.PulseView_backgroundEdgeWidth, DEFAULT_EDGE_WIDTH),
                    typedAttrs.getColor(R.styleable.PulseView_backgroundColor, DEFAULT_BACKGROUND_COLOR),
                    Paint.Style.FILL)
        }

    }
}

internal fun Paint.from(background: Background): Paint {
    strokeWidth = background.edgeWidth
    color = background.color

    return this
}
