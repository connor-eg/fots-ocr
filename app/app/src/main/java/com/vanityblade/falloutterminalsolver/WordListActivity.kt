package com.vanityblade.falloutterminalsolver

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val TAG: String = "WordListActivity"

class WordListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.word_list_display)
        findViewById<FloatingActionButton>(R.id.addWordButton).requestFocus()
        findViewById<FloatingActionButton>(R.id.addWordButton).setOnClickListener { addWordButtonHandler() }
        findViewById<Button>(R.id.solveButton).setOnClickListener { solveButtonHandler() }
        renderWordList()
    }

    private fun addWordButtonHandler() {
        Log.v(TAG, "Add word button clicked")
        TerminalSolver.addWord("wow what a ${Math.random()}")
        renderWordList()
    }

    private fun solveButtonHandler() {
        Log.v(TAG, "Solve button clicked")
        //TODO: Run the solve function in TerminalSolver.kt
    }

    //Should be called any time the list of words is updated.
    private fun renderWordList() {
        val textContainer: LinearLayout = findViewById(R.id.wordListLinearLayout)
        val inflater: LayoutInflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        textContainer.removeAllViews()
        if (TerminalSolver.getWordListSize() == 0) {
            val newView = inflater.inflate(R.layout.word_list_single_word, null)
            newView.findViewById<TextView>(R.id.wordListWord).text =
                getString(R.string.emptyListText)
            textContainer.addView(newView)
        } else {
            for (word in TerminalSolver.getWordList()) {
                //TODO: Display all of the words in solver.wordList in a neat line
                val newView = inflater.inflate(R.layout.word_list_single_word, null)
                newView.findViewById<TextView>(R.id.wordListWord).text = word
                newView.setOnLongClickListener { handleWordDeletion(word) }
                textContainer.addView(newView)
            }
        }
    }

    //Asks the user if they want to delete a word from the list.
    private fun handleWordDeletion(word: String): Boolean {
        //Prompt the user to check if they want to delete the word
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Delete '${word}' from the list?")
            .setCancelable(true)
            .setPositiveButton(R.string.deletionOkButton) { _, _ ->
                run {
                    Log.v(TAG, "OK Button pressed")
                    TerminalSolver.delWord(word)
                    renderWordList()
                }
            }
            .setNegativeButton(
                R.string.deletionCancelButton
            ) { _, _ ->
                run {
                    Log.v(TAG, "CANCEL button pressed")
                }
            }
            .show()
        return true //Forced to return a boolean value here because the listener that calls this requires a boolean??? Not sure why.
    }
}