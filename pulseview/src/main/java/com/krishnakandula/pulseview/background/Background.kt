package com.krishnakandula.pulseview.background

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.krishnakandula.pulseview.R
import com.krishnakandula.pulseview.util.toPx

internal class Background(val edgeRadius: Float,
                          val edgeWidth: Float,
                          val style: Paint.Style,
                          val color: Int) {

    val rect = Rect()
    val paint = Paint().from(this)

    companion object {
        val DEFAULT_EDGE_RADIUS: Float = 16.toPx()
        private val DEFAULT_EDGE_WIDTH: Float = 5.toPx()
        private val DEFAULT_BACKGROUND_COLOR: Int = Color.parseColor("#e6e6e6")

        val minWidth: Float = 250.toPx()
        val minHeight: Float = 250.toPx()

        fun from(typedAttrs: TypedArray): Background {
            return Background(typedAttrs.getFloat(R.styleable.PulseView_edgeRadius, DEFAULT_EDGE_RADIUS),
                    typedAttrs.getFloat(R.styleable.PulseView_edgeWidth, DEFAULT_EDGE_WIDTH),
                    Paint.Style.FILL,
                    typedAttrs.getColor(R.styleable.PulseView_backgroundColor, DEFAULT_BACKGROUND_COLOR))
        }

    }
}

internal fun Paint.from(background: Background): Paint {
    strokeWidth = background.edgeWidth
    color = background.color

    return this
}
