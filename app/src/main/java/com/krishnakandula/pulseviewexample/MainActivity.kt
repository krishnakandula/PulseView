package com.krishnakandula.pulseviewexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.krishnakandula.pulseview.Pulse
import com.krishnakandula.pulseview.point.ColumnAnimationsManager
import com.krishnakandula.pulseview.point.MagnifyAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pulse = createSheet()
        pulseview.setData(pulse)
        pulseview.setAnimationsManager(ColumnAnimationsManager(MagnifyAnimator.createAnimators(pulse.numRows, pulse.numCols)))
        button.setOnClickListener {
            val period = if (periodTextView.text.isNotBlank()) Integer.parseInt(periodTextView.text.toString()).toLong() else 100
            pulseview.getAnimationsManager()?.startAnimations(period, 0, pulse)
        }
        stop_button.setOnClickListener { pulseview.getAnimationsManager()?.stopAllAnimations() }
    }

    private fun createSheet(): Pulse {
        val sheet = Pulse()
        sheet.taps.forEachIndexed { index, row ->
            row.forEachIndexed { colIndex, _ ->
                sheet.taps[index][colIndex] = (Math.random() * 1).roundToInt() == 1
            }
        }
        return sheet
    }

    override fun onStop() {
        pulseview.getAnimationsManager()?.stopAllAnimations()
        super.onStop()
    }
}
