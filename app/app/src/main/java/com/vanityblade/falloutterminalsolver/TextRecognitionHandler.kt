package com.vanityblade.falloutterminalsolver

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

private const val TAG = "TextRecognitionHandler"

object TextRecognitionHandler {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun analyzeImage(bmp: Bitmap) {
        val result = recognizer.process(InputImage.fromBitmap(bmp, 0))
            .addOnSuccessListener { result ->
                run {
                    for(block in result.textBlocks){
                        for(line in block.lines){
                            for(word in line.elements) {
                                Log.v(TAG, "[${word.text}]")
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.v(TAG, "Could not read image: $e")
            }

    }

}