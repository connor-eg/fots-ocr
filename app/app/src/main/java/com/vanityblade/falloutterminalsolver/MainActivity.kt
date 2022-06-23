package com.vanityblade.falloutterminalsolver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

private const val TAG: String = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.optionOCR).setOnClickListener { optionOCRClicked() }
        findViewById<Button>(R.id.optionManual).setOnClickListener { optionManualClicked() }
    }

    private fun optionManualClicked() {
        Log.v(TAG, "manual was clicked")
    }

    private fun optionOCRClicked() {
        Log.v(TAG, "OCR was clicked")
    }
}