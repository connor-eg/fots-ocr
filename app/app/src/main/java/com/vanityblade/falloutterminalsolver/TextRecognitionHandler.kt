package com.vanityblade.falloutterminalsolver

import android.graphics.*
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


private const val TAG = "TextRecognitionHandler"

object TextRecognitionHandler {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun analyzeImage(bmp: Bitmap) {
        //Convert the image to greyscale, maybe that helps the OCR???

        val result = recognizer.process(InputImage.fromBitmap(toGrayscale(bmp)!!, 0))
            .addOnSuccessListener { result ->
                run {
                    for(block in result.textBlocks){
                        Log.v(TAG, "[${block.text}]")
                        for(line in block.lines){
                            for(word in line.elements) {
                                //nothing here for now until I can figure out how to extract valid words.
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.v(TAG, "Could not read image: $e")
            }

    }

    private fun toGrayscale(bmpOriginal: Bitmap): Bitmap? {
        val height: Int = bmpOriginal.height
        val width: Int = bmpOriginal.width
        val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmpGrayscale)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        val f = ColorMatrixColorFilter(cm)
        paint.colorFilter = f
        c.drawBitmap(bmpOriginal, 0F, 0F, paint)
        return bmpGrayscale
    }
}