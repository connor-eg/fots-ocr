package com.vanityblade.falloutterminalsolver

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import android.widget.Toast.makeText
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
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val input = EditText(this)
        dialogBuilder.setView(input)
        dialogBuilder.setMessage("Type a word to add to the list.")
            .setCancelable(true)
            .setPositiveButton(R.string.addWordFloatButton) { _, _ ->
                run {
                    TerminalSolver.addWord(input.text.toString())
                    renderWordList()
                }
            }
            .setNegativeButton(R.string.deletionCancelButton, null)
            .show()
    }

    private fun solveButtonHandler() {
        Log.v(TAG, "Solve button clicked")
        //TODO: Run the solve function in TerminalSolver.kt
        //Validate the words in the list before attempting to solve. Properly formed words may
        // not have numbers and must all be the same length.
        if(TerminalSolver.getWordListSize() <= 2) { //No need to solve if the list is too small.
            simpleMessage("The number of words in the list is too low for automatic solving to be useful.")
            return
        }
        val correctWordLength = TerminalSolver.getWord(0).length
        for(word in TerminalSolver.getWordList()){
            if(word.length != correctWordLength) {
                simpleMessage("At least of the words in the list has a different size than the rest! Please edit that word and retry solving.")
                return
            }
            if(word.contains(Regex("[^A-Za-z]"))){
                simpleMessage("At least one of the words contains non-letter characters! Please edit that word and retry solving.")
                return
            }
        }
        simpleMessage("Valid solvable!")
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
            for (index in 0 until TerminalSolver.getWordListSize()) {
                val word = TerminalSolver.getWord(index)
                val newView = inflater.inflate(R.layout.word_list_single_word, null)
                newView.findViewById<TextView>(R.id.wordListWord).text = word
                newView.setOnLongClickListener { handleWordDeletion(word, index) }
                newView.setOnClickListener { handleWordEdit(word, index) }
                textContainer.addView(newView)
            }
        }
    }

    //Asks the user if they want to delete a word from the list.
    private fun handleWordDeletion(word: String, index: Int): Boolean {
        //Prompt the user to check if they want to delete the word
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Delete '$word' from the list?")
            .setCancelable(true)
            .setPositiveButton(R.string.okButton) { _, _ ->
                run {
                    Log.v(TAG, "OK Button pressed")
                    TerminalSolver.delWord(index)
                    renderWordList()
                }
            }
            .setNegativeButton(R.string.deletionCancelButton, null)
            .show()
        return true //Forced to return a boolean value here because the listener that calls this requires a boolean??? Not sure why.
    }

    private fun handleWordEdit(word: String, index: Int) {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val input = EditText(this)
        input.setText(word)
        dialogBuilder.setView(input)
        dialogBuilder.setMessage("Editing the word '$word'")
            .setCancelable(true)
            .setPositiveButton(R.string.editWordActionText) { _, _ ->
                run {
                    TerminalSolver.editWord(index, input.text.toString())
                    renderWordList()
                }
            }
            .setNegativeButton(R.string.deletionCancelButton, null)
            .show()
    }

    //Tell the user something with a message that has no interaction other than closing it.
    private fun simpleMessage(msg: String){
        makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }
}