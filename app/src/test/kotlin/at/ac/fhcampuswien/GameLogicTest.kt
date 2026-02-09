import at.ac.fhcampuswien.GuessResult
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.test.*

class GameLogicTest {

    private lateinit var gameLogic: GameLogic

    @BeforeTest
    fun setup() {
        gameLogic = GameLogic(4)  // Default 4-digit game
    }

    /** Reflection Tests */

    @Test
    fun `GameLogic class properties are correctly defined`(){
        val properties = GameLogic::class.memberProperties.map { it.name }.toSet()
        assertTrue("digitsToGuess" in properties, "digitsToGuess property should be declared")
        assertTrue("targetNumber" in properties, "targetNumber property should be declared")

        val digitsToGuessProperty = GameLogic::class.memberProperties.find { it.name == "digitsToGuess" }
        assertNotNull(digitsToGuessProperty, "digitsToGuess property should exist")
        assertTrue(digitsToGuessProperty.returnType.toString() == "kotlin.Int", "digitsToGuess should be of type Int")
        assertEquals(digitsToGuessProperty.visibility, KVisibility.PUBLIC, "digitsToGuess should be public")

        // Check if digitsToGuess has a private setter
        assertTrue(digitsToGuessProperty is KMutableProperty<*>, "digitsToGuess should be mutable")
        assertEquals(KVisibility.PRIVATE, digitsToGuessProperty.setter.visibility, "digitsToGuess setter should be private")

        val targetNumberProperty = GameLogic::class.memberProperties.find { it.name == "targetNumber" }
        assertNotNull(targetNumberProperty, "targetNumber property should exist")
        // check contains List<Int>
        assertTrue(targetNumberProperty.returnType.toString().contains("List"), "targetNumber should be a List<Int>")
        assertEquals(targetNumberProperty.visibility, KVisibility.PRIVATE, "targetNumber should be private")
    }

    @Test
    fun `GameLogic class has correct functions and lambdas`() {
        val functions = GameLogic::class.declaredFunctions.map { it.name }.toSet()
        val expectedFunctions = setOf("isWinningGuess")
        assertTrue(expectedFunctions.all { it in functions }, "All expected regular functions should be declared")

        val isWinningGuessFunction = GameLogic::class.declaredFunctions.find { it.name == "isWinningGuess" }
        assertNotNull(isWinningGuessFunction, "isWinningGuess function should exist")
        assertEquals(2, isWinningGuessFunction.parameters.size, "isWinningGuess should have one explicit parameter (instance reference counts as one)")
        assertTrue(isWinningGuessFunction.returnType.toString() == "kotlin.Boolean", "isWinningGuess should return Boolean")

        // Check for lambda functions defined as properties
        val properties = GameLogic::class.memberProperties.map { it.name }.toSet()
        val expectedLambdas = setOf("evaluateGuess", "isValidGuess")
        assertTrue(expectedLambdas.all { it in properties }, "Lambda properties evaluateGuess and isValidGuess should be defined")

        val evaluateGuessProperty = GameLogic::class.memberProperties.find { it.name == "evaluateGuess" }
        assertNotNull(evaluateGuessProperty, "evaluateGuess lambda property should exist")
        // check if evaluateGuess is a lambda
        println(evaluateGuessProperty.returnType.toString())
        assertTrue(evaluateGuessProperty.returnType.toString().contains("GuessResult"), "evaluateGuess should return GuessResult")

        val isValidGuessProperty = GameLogic::class.memberProperties.find { it.name == "isValidGuess" }
        assertNotNull(isValidGuessProperty, "isValidGuess lambda property should exist")
        assertTrue(isValidGuessProperty.returnType.toString().contains("Boolean"), "isValidGuess should return Boolean")
    }

    @Test
    fun `GameLogic companion object properties and functions`() {
        val companionObject = GameLogic::class.companionObject
        assertNotNull(companionObject, "Companion object should exist")

        val companionProperties = companionObject.declaredMemberProperties.map { it.name }
        assertTrue("DEFAULT_DIGITS" in companionProperties, "DEFAULT_DIGITS constant should be defined")

        val defaultDigitsProperty = companionObject.declaredMemberProperties.find { it.name == "DEFAULT_DIGITS" }
        assertNotNull(defaultDigitsProperty, "DEFAULT_DIGITS should exist")

        assertTrue(defaultDigitsProperty.returnType.toString() == "kotlin.Int", "DEFAULT_DIGITS should be of type Int")
        assertEquals(4, defaultDigitsProperty.getter.call(companionObject.objectInstance), "DEFAULT_DIGITS should be 4")

        val companionFunctions = companionObject.declaredFunctions.map { it.name }
        assertTrue("generateRandomNumber" in companionFunctions, "generateRandomNumber should be defined")

        val generateRandomNumberFunction = companionObject.declaredFunctions.find { it.name == "generateRandomNumber" }
        assertNotNull(generateRandomNumberFunction, "generateRandomNumber function should exist")
        assertEquals(2, generateRandomNumberFunction.parameters.size, "generateRandomNumber should have one parameter plus instance")

        val instanceParam = generateRandomNumberFunction.parameters[0]
        val digitsParam = generateRandomNumberFunction.parameters[1]

        assertTrue(instanceParam.type.toString().contains("Companion"), "First parameter should be companion instance")
        assertTrue(digitsParam.type.toString() == "kotlin.Int", "Second parameter should be an Int")

        // Test generateRandomNumber functionality
        val generatedList = generateRandomNumberFunction.call(companionObject.objectInstance, 4) as List<*>
        assertEquals(4, generatedList.size, "Generated list must have 4 digits")
        assertTrue(generatedList.all { it is Int && it in 1..9 }, "Generated digits must be between 1 and 9")
        assertTrue(generatedList.toSet().size == 4, "Generated digits must be unique")
    }

    /** Functional Tests */

    @Test
    fun `test evaluateGuess calculates correct digits and positions`() {
        val targetNumber = listOf(8, 3, 9, 6)

        val targetNumberField = gameLogic::class
            .memberProperties.find { it.name == "targetNumber" }
            ?.apply { isAccessible = true }
            ?.javaField // needed because targetNumber is immutable and has no setter

        targetNumberField?.apply { isAccessible = true }?.set(gameLogic, targetNumber)

        val result = gameLogic.evaluateGuess("8396")
        assertEquals(4, result.correctDigits, "All digits should match in any order")
        assertEquals(4, result.correctPositions, "All digits should be in correct positions")
    }

    @Test
    fun `test isValidGuess correctly validates input`() {
        assertTrue(gameLogic.isValidGuess("1234"), "Valid guess must pass")
        assertFalse(gameLogic.isValidGuess("12345"), "Guess with too many digits must fail")
        assertFalse(gameLogic.isValidGuess("12a4"), "Guess with non-digit characters must fail")
        assertFalse(gameLogic.isValidGuess("1122"), "Guess with repeated digits must fail")
        assertFalse(gameLogic.isValidGuess(null), "Null guess must fail")
    }

    @Test
    fun `test isWinningGuess correctly identifies win`() {
        val result = GuessResult(4, 4)
        assertTrue(gameLogic.isWinningGuess(result), "Guess with all correct digits and positions should win")

        val incorrectResult = GuessResult(4, 3)
        assertFalse(gameLogic.isWinningGuess(incorrectResult), "Guess with incorrect positions should not win")
    }

}
