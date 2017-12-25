package com.krishnakandula.pulseview.linetab

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.krishnakandula.pulseview.background.Background
import com.krishnakandula.pulseview.toPx

internal class LineTab {

    val rect = Rect()

    companion object {
        const val DEFAULT_COLOR = Color.DKGRAY
        val MIN_WIDTH = Background.minWidth
        val HEIGHT = 40.toPx()
        val HEIGHT_OFFSET = 40.toPx()
    }
}

internal fun Paint.from(lineTab: LineTab): Paint {
    this.color = LineTab.DEFAULT_COLOR
    this.style = Paint.Style.FILL_AND_STROKE
    return this
}
