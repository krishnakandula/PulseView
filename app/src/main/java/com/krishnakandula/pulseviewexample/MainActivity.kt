package com.krishnakandula.pulseviewexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.krishnakandula.pulseview.Pulse
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pulseview.setData(createSheet())
        button.setOnClickListener {
            pulseview.animationsManager.startAnimations(period = 50, delay = 0)
        }
        stop_button.setOnClickListener { pulseview.animationsManager.stopAllAnimations() }
    }

    private fun createSheet(): Pulse {
        val sheet = Pulse()
//        sheet.taps.forEachIndexed { index, row ->
//            row.forEachIndexed { colIndex, _ ->
//                sheet.taps[index][colIndex] = (Math.random() * 1).roundToInt() == 1
//            }
//        }
        sheet.taps.forEachIndexed { index, row ->
            if (index == 0) row.forEachIndexed { index, _ -> row[index] = true}
        }
        return sheet
    }

    override fun onStop() {
        pulseview.animationsManager.stopAllAnimations()
        super.onStop()
    }
}
