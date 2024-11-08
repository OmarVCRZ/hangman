
/*
@Program:
Hangman Game

@Description:
The program is Kotlin based version of Hangman. A well-known guessing game, where the objective is to figure-out
the random word selected from a list. The developer of this model program, included a hint mechanism and a limited
amount of attempts to guess the word, from two choices either by selecting words one at a time, or guessing
the full word, but the cost of losing attempts. After the user guesses at least four times, they will receive a hint.
At the end of the game, the player will be given an option to either play the game again or not.

@Author:
Omar Diaz Martinez

@Group: CIS476 Software Architecture [Kotlin Team]
Created: 11/07/2024
 */

// Sets the game by creating a list of words and calling startHangman() function to begin
fun main() {

    val listOfWords = mapOf(
        // Created a map that the key-value pairs has the word and its hint.
        "Kotlin" to "A Popular Programming Language used in Android",
        "Software Architecture" to "Architecture",
        "PHP" to "A Programming Language used to Script Websites",
        "Developer" to "Someone who writes code",
        "CIA Triad" to "A Common Model used to Form the Development of Security Systems",
        "Database" to "Stores information of an organization",
        "Hardware" to "Physical Parts of a Computer",
        "Apple" to "Type of Fruit",
        "Red" to "Type of Color",
        "Cat" to "Type of Pet",
        "Plane" to "Type of Transportation that is Known to be Fast",
        "Rain" to "Type of Weather Environment",
        "Happy" to "Type of Emotion",
        "City" to "Type of Location",
        "Burger" to "Type of Fast Food",
        "Chair" to "Type of Furniture",
        "Tiger" to "Famous Song from Survivor, Eye of the...",
        "Bus" to "School Transportation",
        "Testing" to "Checks if the Code is Works",
        "Packages" to "Bundle of Classes",
        "Firewall" to "A Network Security Wall",
        "Upload" to "Sends Data Online",
        "Workflow" to "Steps to Accomplish Task",
        "FrontEnd" to "Client Side of the Application",
        "BackEnd" to "Server Side of the Application",
        "Client" to "Device that Request Information from a Server",
        "Server" to "Device that Provides Services to a Client",
        "Hangman" to "A Word-Guessing Game",
        "Boolean" to "A data-type that has two-values: True or False",
        "Loop" to "A Sequence-of-Instructions that Continues to Repeat " +
                "Until a Specific Condition is Met",
        "GitHub" to "Web-Based Platform that allows devs to store, collaborate," +
                " and track changes"
    )
    do {
        // Calls the function (startHangman) to start the game
        startHangman(listOfWords)

        // Asking the user if they want to play again
        println("Would you like to play again? [y/n]: ")
        // Read's user input, to ensure it's lowercase, removes whitespace trails
        val startAgain = readln().trim().lowercase()
    } while (startAgain == "y") // Hangman will continue to run as long as the user select 'y'
    // If user does not wish to play again, it exits the loop
    println("Thank you for playing our version of HangMan! Goodbye!")
}

// Main Purpose is to run the Hangman Game
fun startHangman(listOfWords: Map<String, String>) {

    println("Welcome to Hangman!") // ISSUE: Maybe Create a Form to connect to the User

    // Selects a random key-value pair from listOfWords, assigns the key to the var (guessWords),
    // and assigns the value to the var (hint)
    val (guessWords, hint) = listOfWords.entries.random() // entries gives you access to all set key-value pairs.

    // Setting the number of allowed attempts the user gains
    var amountLeft = 7

    // Creating a MutableList (https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/)
    // it allows you to modify elements (change throughout the game, when users guesses incorrectly)
    // lambda expression defines each element in the MutableList, initializing it as an underscore
    val wordShape = MutableList(guessWords.length) {'_'} // representing a letter that the user needs to guess

    // Keeps track of all the incorrect guesses (Sets does not allow duplication)
    val errorGuess = mutableSetOf<Char>()
    // val errorGuess = mutableListOf<Char>() --first approach incorrect due to it being a list (duplication occurs).

    var gameWon = false // Created a variable to track if the user won the game

    // Game Continues till User Run-Out of Guesses or has not guessed yet
    while (amountLeft > 0 && wordShape.contains('_')) {

        // Displaying the Game's Current State of Play
        outputGameCondition(wordShape, errorGuess, amountLeft)
        // Asking the user a choice to either guess the full word or do a single letter
        println()
        println("Would you like to guess the full word or do a single letter at a time?")
        println("Note: Attempting full world penalizes you by reducing 2 attempts")
        println()
        println("Please enter your choice [Enter 'w' or 'l']: ")

        // Read's user input, to ensure it's lowercase, removes whitespace trails
        val choice = readln().trim().lowercase()

        // If the user's choice is to select the full word
        if (choice == "w") {
            println("Enter your full world guess: ")
            // Read's user input, to ensure it's lowercase, removes whitespace trails
            val wordGuess = readln().trim().lowercase()

            // Verifies if the users full-worded guess aligns with the targeted word
            if (wordGuess == guessWords.lowercase()) {
                println("Congrats! You've guessed the word correctly")
                gameWon = true // Mark's the user's game as a win
                break // exits loop
            } else {
                println("Failed! You've guessed the word incorrectly [Lost 2 Guesses]")
                amountLeft -= 2 // Reduces the attempts left
            }
        } else { // If the user selected a single letter instead of the whole word

            println("Guess a letter: ")

            // Getting/validating user's guess
            val guess = getUserGuess()

            // Ensures that the guess is a valid single letter
            if (guess == null || !guess.isLetter()) {
                println("Ensure you enter a valid letter! [No #'s, or special characters] Try Again!")
                continue // If input is invalid it goes to the next iteration
            }

            // Checks if the user's current guess has not been made yet (incorrect or correct)
            if (guess in errorGuess || wordShape.contains(guess)) {
                println("You've Already Guessed this! Try A Different Input")
            }
            // Gives a hint after the fourth attempt
            if (errorGuess.size >= 2) {
                println("Seems you need help, here's a hint: $hint")
            }

            var isGuessCorrect = false // The variable starts with a false value each time the users starts a new guess
            // Makes sure that guess does not have any special characters
            if (guess.isLetter()) {
                // Loops through each character in the user's guess
                for (char in guess.lowercase()) { // Converts guess to lowercase
                    // Checks if the current character exists in guessWords (contains a specific char)
                    // Ignore case: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/equals.html
                    if (guessWords.contains(
                            char,
                            ignoreCase = true
                        )
                    ) { // makes comparison case-sensitive ('C' will match 'c')
                        updateWordShape(guessWords, guess, wordShape)
                        isGuessCorrect = true // This means at least one of the characters of the current guess was correct.
                    } else {
                        // if the character is not a part of word (incorrect guess)
                        // Checks if the character from the previous guess has not been guessed incorrectly (errorGuess set)
                        if (char !in errorGuess) {
                            // Updates the users attempts left (reduces only for incorrect guesses)
                            amountLeft = handleErrorGuess(guess, errorGuess, amountLeft)
                        }
                    }
                }
                // Guessed the letter from the word correctly displayed prompt
                if (isGuessCorrect) {
                    println("You Guessed a Letter Correctly!")
                }
            }
        }
    }
    // If the game has not finished (won) yet, then call the function below to show the final outcome of the game
    if (!gameWon) {
        finishedGameCondition(wordShape, guessWords) // Calls the finishedGameCondition after the loop
    }
}

// Main purpose is to output the current game status
fun outputGameCondition(wordShape: MutableList<Char>, errorGuess: MutableSet<Char>, amountLeft: Int) {
    // Displays current state of the word being guessed, then shows which ones are guessed correctly,
    // which is still hidden by "_"
    // Joins the elements of wordShape list into a single string, each character seperated by a space
    println("Word: ${wordShape.joinToString(" ")}")
    // Displays the list of incorrect guesses from the user
    // Joins the elements of wordShape list into a single string, each character seperated by a comma and space
    println("Incorrect Guesses: ${errorGuess.joinToString(", ")}")
    // Displays the number of attempts left
    println("Attempts Left: $amountLeft")
}

// Main purpose is to prompt the user to guess a letter and returns the guess
fun getUserGuess(): Char? { // nullable type (can hold a Char value or null
    // Read's user input, to ensure it's lowercase, removes whitespace trails, and it a type of char
    // Extracts the user's guess as a single character
    return readln().trim().lowercase().firstOrNull()

}

// Main purpose is to update the displayed word shape of the current guessed letters
fun updateWordShape(guessWords: String, guess: Char, wordShape: MutableList<Char>) {
    // Iterating over each index of the string guessWords
    for (i in guessWords.indices) { // returns a range representing indices (Guess word: "Cat", returns [0, 1, 2] spaces)
        // Checks first character of the string in the guess if it matches in the guessWords list,
        // ignoring case sensitivity
        if (guessWords[i].equals(guess, ignoreCase = true)) {
            // If it matches, it will update the wordShape list at index i [correct guessed letter],
            // when user guess correctly the index of wordShape will update to the letter from guessWords
            wordShape[i] = guessWords[i]
        }
    }
}

// Main purpose is to handle incorrect guesses by updating the list of the incorrect guesses and also reduce the attempts
fun handleErrorGuess(guess: Char, errorGuess: MutableSet<Char>, amountLeft: Int): Int { // return type of the function
    // Anytime a player guesses a letter that does not match the word, the letter is added to errorGuess
    errorGuess.add(guess)  // Adds the value of guess to the errorGuess collection
    println("Please Try Again.")
    // Decreases the attempts left
    return amountLeft -1
}

// Main purpose is to output the final game result
fun finishedGameCondition(wordShape: MutableList<Char>, guessWords: String) {
    // If there is no underscores left on wordShape (meaning its fully completed) the player wins, if not player loses.
    if (!wordShape.contains('_')) {
        println("Congratulations! You've Guessed the Word Correctly: $guessWords")
    } else {
        println("Game Over! You Ran Out of Attempts. The Correct Word was : $guessWords")
    }
}






