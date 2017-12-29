package com.krishnakandula.pulseviewexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.krishnakandula.pulseview.Sheet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pulseview.setData(createSheet())
        button.setOnClickListener { pulseview.startAnimationsInRangeWithDelay(0, 7, 50) }

    }

    private fun createSheet(): Sheet {
        val sheet = Sheet()
        (0 until sheet.taps.size).forEach { sheet.taps[it][0] = true }

        return sheet
    }
}
