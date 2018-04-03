package com.krishnakandula.pulseviewexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.krishnakandula.pulseview.Pulse
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pulseview.setData(createSheet())
        button.setOnClickListener {
            pulseview
                    .animationsManager
                    .startAnimationsInRangeWithDelay(0, 7, 500)
        }
        stop_button.setOnClickListener { pulseview.animationsManager.stop() }
    }

    private fun createSheet(): Pulse {
        val sheet = Pulse()
        (0 until sheet.taps.size).forEach { sheet.taps[it][0] = true }

        return sheet
    }

    override fun onStop() {
        pulseview.animationsManager.stop()
        super.onStop()
    }
}
