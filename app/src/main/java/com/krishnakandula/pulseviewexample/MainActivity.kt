package com.krishnakandula.pulseviewexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.krishnakandula.pulseview.Pulse
import com.krishnakandula.pulseview.PulseView
import com.krishnakandula.pulseview.point.animationmanager.AnimationsManager
import com.krishnakandula.pulseview.point.animationmanager.ColumnAnimationsManager
import com.krishnakandula.pulseview.point.animationmanager.PulseAnimationsManager
import com.krishnakandula.pulseview.point.pointanimator.MagnifyAnimator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pulse = createSheet(pulseview)
        val magnifyAnimators = MagnifyAnimator.createAnimators(pulse.horizontalLines + 1, pulse.verticalLines + 1)
        pulseview.setData(pulse)
        button.setOnClickListener {
            val period = if (periodTextView.text.isNotBlank()) Integer.parseInt(periodTextView.text.toString()).toLong() else 175
            val animationsManager = pulseview.getAnimationsManager()
            when (animationsManager) {
                is ColumnAnimationsManager -> animationsManager.startAnimations(period, 0, pulse)
                is PulseAnimationsManager -> animationsManager.startAnimations(period, 0, pulse, 0, pulse.horizontalLines, 50, false)
            }
        }
        stop_button.setOnClickListener { pulseview.getAnimationsManager()?.stopAllAnimations() }
        anim_manager_spinner.adapter = ArrayAdapter.createFromResource(this, R.array.animation_managers, android.R.layout.simple_spinner_dropdown_item)
        anim_manager_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                pulseview.setAnimationsManager(PulseAnimationsManager(magnifyAnimators))
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val choices = resources.getStringArray(R.array.animation_managers)
                if (anim_manager_spinner.selectedItem == null) {
                    PulseAnimationsManager(magnifyAnimators)
                } else {
                    pulseview.setAnimationsManager(when (anim_manager_spinner.selectedItem as String) {
                        choices[0] -> PulseAnimationsManager(magnifyAnimators)
                        choices[1] -> ColumnAnimationsManager(magnifyAnimators)
                        else -> ColumnAnimationsManager(magnifyAnimators)
                    })
                }
            }
        }
    }

    private fun createSheet(pulseView: PulseView): Pulse {
        val sheet = Pulse(pulseView.getVerticalLines(), pulseView.getHorizontalLines())
        sheet.taps.forEachIndexed { index, row ->
            row.forEachIndexed { colIndex, _ ->
                sheet.taps[index][colIndex] = false
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
