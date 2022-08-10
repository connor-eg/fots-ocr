package com.vanityblade.falloutterminalsolver

import android.util.Log

object TerminalSolver {
    //The list of words
    private var wordList: MutableList<String> = mutableListOf()

    //When the list is finalized
    private var noMoreEliminations: Boolean = false

    //The meat of the solver. Returns the index of the best word to use
    fun solve(): Int {
        var matchNumbers: MutableList<Int> = mutableListOf()
        for (i in 0..wordList.size) { matchNumbers.add(0) }
        var lowestVariance: Double = Double.POSITIVE_INFINITY //The lowest variance found so far
        var lowestVarianceIndex: Int = -1 //The value to be returned
        //Generate and use match numbers for each word
        for (i in 0 until wordList.size) {
            matchNumbers = matchNumbers.map{0} as MutableList<Int> //Empty out the list of matchNumbers for each word to recalculate variance
            for (k in 0 until wordList.size) {
                if (i == k) continue //Don't compare a word against itself
                matchNumbers[calcSimilarity(wordList[i], wordList[k])]++
            }
            val curVariance = calcVariance(matchNumbers)
            if (curVariance < lowestVariance) {
                lowestVariance = curVariance
                lowestVarianceIndex = i
            }
        }

        return lowestVarianceIndex
    }

    //Shortens the word list, eliminating words that do not have the correct match number
    //  with the given word
    fun eliminate(compareWord: String, matchNumber: Int): TerminalSolver {
        val wordsToEliminate: MutableList<Int> = mutableListOf()
        for (i in 0 until wordList.size) {
            if (calcSimilarity(compareWord, wordList[i]) != matchNumber) {
                wordsToEliminate.add(i) //The list SHOULD be in sorted order from smallest to largest index at this point
            }
        }

        //Why is it an index when the items are on their way out???
        for (index in wordsToEliminate.reversed()) {
            delWord(index)
        }
        return this
    }

    //Finds the similarity between two words
    private fun calcSimilarity(word1: String, word2: String): Int {
        var similarity = 0
        val comp1 = word1.lowercase()
        val comp2 = word2.lowercase()
        for (i in 0 until word1.length) {
            if (comp1[i] == comp2[i]) similarity++
        }
        return similarity
    }

    //Calculate the variance of a set of integers
    private fun calcVariance(matchNumbersINT: MutableList<Int>): Double {
        //Converting ints to doubles the stupid way
        val matchNumbers: MutableList<Double> = mutableListOf()
        for (num in matchNumbersINT) {
            matchNumbers.add(num.toDouble())
        }
        var variance = 0.0 //Result of below calculations

        //Get the mean of the input numbers
        for (num in matchNumbers) {
            variance += num
        }
        variance /= matchNumbers.size

        //Store each value minus the mean into matchNumbers, then square them
        for (i in 0 until matchNumbers.size) {
            matchNumbers[i] -= variance
            matchNumbers[i] *= matchNumbers[i]
        }

        //Get the sum of the squares
        variance = 0.0
        for (num in matchNumbers) {
            variance += num
        }

        //Normally, to calculate variance at this point you would divide the sum of squares just
        //  calculated by matchNumbers.length. However, we can omit this because we only care
        //  which word has the smallest variance (which wouldn't change after this calculation)
        return variance
    }

    fun getWordList(): MutableList<String> {
        return wordList
    }

    fun getWordListSize(): Int {
        return wordList.size
    }

    //Lots of helper functions
    fun editWord(oldWord: String, newWord: String): TerminalSolver {
        val i: Int = wordList.indexOf(oldWord)
        if (i == -1) return this
        wordList[i] = newWord
        return this
    }

    fun editWord(oldWordIndex: Int, newWord: String): TerminalSolver {
        wordList[oldWordIndex] = newWord
        return this
    }

    fun addWord(newWord: String): TerminalSolver {
        wordList.add(newWord)
        Log.v("TerminalSolver", "Added the word [$newWord], word count is now ${wordList.size}")
        return this
    }

    fun delWord(index: Int): TerminalSolver {
        wordList.removeAt(index)
        return this
    }

    fun delWord(wordToDel: String): TerminalSolver {
        val i: Int = wordList.indexOf(wordToDel)
        if (i == -1) return this
        wordList.removeAt(i)
        return this
    }

    fun getWord(index: Int): String {
        return wordList[index]
    }

}