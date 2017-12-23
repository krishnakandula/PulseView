package com.krishnakandula.pulseview

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint



internal class Background(var edgeRadius: Float,
                          var edgeWidth: Float,
                          var style: Paint.Style,
                          var color: Int) {

    companion object {
        private val DEFAULT_EDGE_RADIUS: Float = 16.toPx()
        private val DEFAULT_EDGE_WIDTH: Float = 5.toPx()
        private val DEFAULT_BACKGROUND_COLOR: Int = Color.parseColor("#e6e6e6")

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
