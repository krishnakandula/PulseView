package com.krishnakandula.pulseviewexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.krishnakandula.pulseview.Pulse
import com.krishnakandula.pulseview.PulseView
import com.krishnakandula.pulseview.point.animationmanager.ColumnAnimationsManager
import com.krishnakandula.pulseview.point.animationmanager.PulseAnimationsManager
import com.krishnakandula.pulseview.point.pointanimator.MagnifyAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pulse = createSheet(pulseview)
        pulseview.setData(pulse)
        pulseview.setAnimationsManager(PulseAnimationsManager(MagnifyAnimator.createAnimators(pulse.horizontalLines + 1, pulse.verticalLines + 1)))
        button.setOnClickListener {
            val period = if (periodTextView.text.isNotBlank()) Integer.parseInt(periodTextView.text.toString()).toLong() else 175
//            (pulseview.getAnimationsManager() as ColumnAnimationsManager).startAnimations(period, 0, pulse)
            (pulseview.getAnimationsManager() as PulseAnimationsManager).startAnimations(period, 0, pulse, 0, pulse.horizontalLines, 50, false)
        }
        stop_button.setOnClickListener { pulseview.getAnimationsManager()?.stopAllAnimations() }
    }

    private fun createSheet(pulseView: PulseView): Pulse {
        val sheet = Pulse(pulseView.getVerticalLines(), pulseView.getHorizontalLines())
        sheet.taps.forEachIndexed { index, row ->
            row.forEachIndexed { colIndex, _ ->
                sheet.taps[index][colIndex] = true
//                sheet.taps[index][colIndex] = (Math.random() * 1).roundToInt() == 1
            }
        }
        return sheet
    }

    override fun onStop() {
        pulseview.getAnimationsManager()?.stopAllAnimations()
        super.onStop()
    }
}
