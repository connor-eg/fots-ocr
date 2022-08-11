package com.vanityblade.falloutterminalsolver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts




private const val TAG: String = "MainActivity"

class MainActivity : AppCompatActivity() {
    //ActivityResultLauncher used when getting a picture for OCR purposes.
    private val getImageLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { res -> run{
        Log.v(TAG, "GOT BACK [$res]")
        TextRecognitionHandler.analyzeImage(res)
    }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.optionOCR).setOnClickListener { optionOCRClicked() }
        findViewById<Button>(R.id.optionManual).setOnClickListener { optionManualClicked() }
    }

    private fun optionManualClicked() {
        Log.v(TAG, "manual was clicked")
        /**
         * 1. Show the user an empty list with "add word" button
         * 2. Allow the user to add an arbitrary number of words
         * 3. The user clicks a "solve" button to begin solving
         * 4. Refer to solver class
         */
        //Change activity to the word list screen
        val intent = Intent(this@MainActivity, WordListActivity::class.java)
        startActivity(intent)
    }

    private fun optionOCRClicked() {
        Log.v(TAG, "OCR was clicked")
        getImageLauncher.launch(null)
        //* Handling the image is done in the event handler for getImageLauncher above. *//
        /**
         * 1. Prompt the user for a picture (either saved or camera)
         * 2. Attempt OCR on the picture, generating a list of words
         * 3. Take the list of words and move to the manual input screen with
         *    those words pre-filled
         */
    }
}