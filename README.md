# fots-ocr
Fallout Terminal Solver with OCR

## What does it do?
This is an Android app that, when it is complete, will take images of the terminal minigame from the Fallout series, and recommend the best word to pick until the minigame is solved. For fans of Fallout who are bored to tears with the minigame, this provides a more interesting way to breeze through it than just clicking random words and hoping for the best (or installing mods that bypass it entirely).
Update on this goal: The OCR implementation via ML Kit is... terrible. It mixes up letters constantly (not great), inconsistently recognizes symbols (bad), and sometimes picks up lines out of order (very bad). I have been coming up with ideas to make it better, but I don't think I'll ever be able to finagle it into working the way I originally intended.

## How does it work?
Boring time!
The terminal minigame can be described like this: Given a list of words, pick the correct word. When an incorrect word is picked, you only learn how many letters the correct word has in common with your word (meaning the same letter in the same position).
For example, given the words MIGHT and SIGHT, these have four letters in common (\_IGHT). WORDS and SWORD have zero letters in common (_____). Not knowing the correct word in advance, my program attempts to provide the best guess to the user.
So how do I define the best guess? In this case, it means the word that eliminates the most other words at once from the list regardless of the actual correct word. 
Here's a puzzle. Consider the three "words" AAAAA, AAAAZ, and ZAAAA. You have two guesses, and the correct word is never the first one you pick. It has an equal chance to be either of the words you did not pick.
Under the rules above, if you pick AAAAA, the number you get back is always four. This tells you nothing about whether AAAAZ or ZAAAA is correct.
By contrast, if you pick AAAAZ, it has four letters in common with AAAAA and three in common with ZAAAA. This means that when you get the number back you know for certain which word is correct.
My program does this on a larger scale. Solving works like this:
1. For each word in the list, count all of the letters it has in common with every other word. I call these "match numbers"
    For the AAAAA/AAAAZ/ZAAAA example above, AAAAZ's match numbers would be 0/0/0/1/1. This is because there are zero words that it shares 0, 1, or 2 letters with, and 1 word that it shares 3 and 4 letters with.
    For AAAAA, the match numbers would be 0/0/0/0/2.
2. Once you have this list of match number sets, calculate the mathematical variance in every set. This is effectively a measure of the "bumpiness" of the list if it were pictured as a bar graph.
3. The list with the lowest variance is the best word (in the event of a tie, suggest the earliest word with the lowest variance). This is because a "flat" list of numbers indicates that the most words are eliminated on average, *regardless of the actual correct number*.
4. To complete the loop, after a word is suggested the user provides the number of letters the correct word actually had in common with the guess word. The solver then eliminates all words that do not match the given criteria and the list can be guessed on again.

## Is this the most efficient solution?
No. This runs in O(n^2) time, which in my view is acceptable because the game doesn't provide more than a few dozen words per minigame. If this project were scaled up for a larger but similar game - Wordle - there are other, more efficient implementations.
As an example for Wordle specifically, I highly recommend looking the mathematician Grant Sanderson's video on Information Theory, available at https://www.youtube.com/watch?v=v68zYyaEmEA

## But why make this???
This project is intended to test both my capabilities in Kotlin/Android Studio, and my ability to learn new technologies that I have never encountered before (specifically, Google's implementation of OCR for Android).

## TERMS OF USE
* You may not redistribute/modify any aspect of this project for commercial purposes.
* You may redistribute/modify this project for non-commercial purposes, provided that you prominently display a link to this GitHub page containing the original project.

Exceptions to the above rules apply *at my discretion*. Feel free to contact me at connoregarcia@gmail.com for more information.
