package com.krishnakandula.pulseviewexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { Thread(Runnable { (0..7).forEach {
            i -> pulseview.startAnimation(i)
            Thread.sleep(100)
        } }).start() }

    }
}
