package com.vanityblade.falloutterminalsolver

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

private const val TAG: String = "WordListActivity"

class WordListActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.word_list_display)
    }
}