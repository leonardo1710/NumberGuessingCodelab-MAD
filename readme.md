[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/OfWerPgu)
# MAD - Codelab 01
## Tasks
* Complete the tutorials **[Introduction to programming in Kotlin](https://developer.android.com/courses/pathways/android-basics-compose-unit-1-pathway-1)** and **[Kotlin fundamentals](https://developer.android.com/courses/pathways/android-basics-compose-unit-2-pathway-1
)**.
* Accept the classroom assignment.
* Clone the repository to your local machine and solve the tasks.
* Test your solution with the provided tests. **Attention:** not all tests will compile until you implement the required functions. Write you solution first. Name your functions and variables as described in the tasks.
* Answer comprehension questions for a better understanding.

## Number Guessing Game in Kotlin
The game is a simple number guessing game. The task is to generate a random, max 9-digit, number. The number must **not contain repeating digits**. Valid digits are 1-9.
Ask the user to guess the max 9-digit number. The game is finished when the user guesses the correct digits in the correct order.
In each round, the user gets feedback about the number of correct digits and the number of correct digits in the correct position.
The output should be in the format "n:m", where "n" is the number of digits guessed correctly regardless of their position, 
and "m" is the number of digits guessed correctly at their correct position. Here are some examples:

This example shows the game flow with 4-digits to guess (the default argument)

Random number: [5, 1, 2, 4]
````console
Welcome to the Number Guessing Game!
Guess a 4-digit number with unique digits.
Enter your guess (4 unique digits) or type 'exit': 1234
GuessResult(correctDigits=3, correctPositions=1)
Enter your guess (4 unique digits) or type 'exit': 1111
Invalid guess.
Enter your guess (4 unique digits) or type 'exit': 12345
Invalid guess.
Enter your guess (4 unique digits) or type 'exit': 5126
GuessResult(correctDigits=3, correctPositions=3)
Enter your guess (4 unique digits) or type 'exit': 5124
GuessResult(correctDigits=4, correctPositions=4)
Congratulations! You guessed the correct number.
Do you want to play again? (y/n): n
Thanks for playing!
````

Run the project with `./gradlew run` and test your implementation with the provided tests in `src/test/kotlin/AppTest.kt` with `./gradlew test`.

# Codelab 1 Tasks

## 1. Task
Define a class ``GameLogic`` inside `/GameLogic.kt` with a **primary constructor parameter** `initialDigitsToGuess` of type `Int` which will store the number of digits the user has to guess.
Furthermore, define a public property **digitsToGuess** which is set to value of ``initialDigitsToGuess``. It should have a public getter but a private setter. Use built-in Kotlin getters and setters.

## 2. Task
Define a companion object inside the `GameLogic` class containing:
- a constant ``DEFAULT_DIGITS`` to hold the default number of digits.
- a function ``generateRandomNumber(digits: Int): List<Int>`` to generate a random list of digits without any repeating digits. It should return a list of random digits from 1 to 9 with the specified number of digits.
- set the default value of ``initialDigitsToGuess`` to `DEFAULT_DIGITS`.

## 3. Task
Inside ``GameLogic`` class, create a `targetNumber` property of type `List<Int>` that will store the randomly generated number. This should be generated when the class is instantiated using the `generateRandomNumber` function.

## 4. Task
Create a **data class** ``GuessResult`` with private constructor parameters ``correctDigits`` and `correctPositions` of type `Int`. Make the properties **immutable**.
```GuessResult``` represents the result of a guess evaluation.
Override the ``toString`` method to return the result in the format "n:m", where "n" is the number of correct digits regardless of their position, and "m" is the number of correct digits in the correct position.

## 5. Task
Inside ``GameLogic`` class, create a **lambda function** property `evaluateGuess` that takes a string input (user guess) and returns a ``GuessResult`` with two values:
- the number of correct digits (not necessarily in the correct position)
- the number of correct digits in the correct position
- the lambda should convert the guess (string) into a list of integers and compare it to the targetNumber to determine the number of correct digits and correct positions.

## 6. Task
Implement another lambda function property ``isValidGuess`` that checks whether the user's guess is valid. It should have a parameter (user guess) of type **nullable** `String` and a `Boolean` return value. A valid guess is:
- a string of the same length as ``digitsToGuess``
- consists only of digits (1-9)
- has no repeating digits
- if the provided string is **null** or invalid, the function should return ``false``, otherwise `true`.

## 7. Task
Implement an **expression body function** `isWinningGuess` inside ``GameLogic`` class. Give it a parameter of type `GuessResult`. It should return `true` if all digits are guessed correctly (digits and positions) and ``false`` otherwise. An expression body function is a function that consists of a single expression that is evaluated and returned without using curly braces or a return statement.

## 8. Task
Create a new file containing a ``GameManager`` object that manages the game flow for the number guessing game. This object should handle user interaction, game loop management and game state evaluation, integrating with the existing `GameLogic` class.
Features of ``GameManager``:
- private immutable property gameLogic which is initialized with a new instance of ``GameLogic``.
- public **start** function without return value. It initiates the game and maintains the game loop:
  1. It displays a welcome message explaining the game rules
  2. It calls ``getUserGuess`` (see below) to get the user's guess
  3. It validates the guess using ``gameLogic.isValidGuess``
  4. Afterwards, calls ``gameLogic.evaluateGuess`` to evaluate the guess. Display the result in the format "n:m", where: ``n`` is the number of correct digits in any position and ``m`` is the number of correct digits in the correct position.
  5. Finally, it checks if the guess is correct using ``gameLogic.isWinningGuess`` and displays the result. If the guess is correct, display a congratulatory message and prompt the user to restart or quit. End the game gracefully when the player chooses not to restart.
  - Use a loop to continuously prompt the player for guesses until they correctly guess the number.
- private **getUserGuess** function returning a **nullable** ``String``: prompt the user to enter a guess. If the guess is ``exit``, it gracefully ends the program. Otherwise, it returns the user's guess.

# Final Project Structure
The project is structured into two main Kotlin files:

**App.kt**: contains ``main`` function and calls `GameManager.start`

**GameManager.kt**: contains ``GameManager`` object

**GameLogic.kt**: contains main game logic in ``GameLogic`` class.

**GuessResult**: contains data class ``GuessResult``.

**AppTest.kt**: Contains unit tests for the various functions in App.kt.


# Comprehension Questions
## Nullability
In Java, variables can hold null values, but in Kotlin, nullability is more explicit.
- How does Kotlin handle nullability compared to Java?
- How would you declare a nullable variable in Kotlin, and how would you handle it safely?

## Immutable vs Mutable Variables and Collections
Kotlin encourages immutability. 
- What is the difference between val and var in Kotlin, and when would you use each one?
- Explain the differences between two ways of calling functions of nullable types in Kotlin: **safe call operator** and **not-null assertion**.
- What is the difference between listOf() and mutableListOf() in Kotlin?

## Data Classes
- How do data classes in Kotlin differ from regular classes in Java, and what are some key benefits?
- How would you define a data class in Kotlin?

## Lambda Expressions and Higher-Order Functions
- Explain how lambda expressions work in Kotlin, and how they can be used to create higher-order functions.
- How would you pass a function as a parameter to another function?
- Why would you store a function inside a variable?

## Companion Objects and Objects
- What is the difference between object and companion object in Kotlin? When would you use each?
- Why and when do we use ``object`` declaration in Kotlin?
- Why and when do we use ``companion objects`` in Kotlin? How does it compare to Java?

## Default Parameters
- How does Kotlin handle default parameter values, and how does this differ from method overloading in Java?
- Give an example of a function with default parameters

## Constructors
In Kotlin, constructors are part of the class definition. 
- What are the differences between primary and secondary constructors and how are they declared?




