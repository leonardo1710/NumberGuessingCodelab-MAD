package at.ac.fhcampuswien

import GameManager
import kotlin.reflect.full.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.*

class GameManagerTest {
    private val standardOut = System.out
    private val standardIn = System.`in`
    private lateinit var outputStreamCaptor: ByteArrayOutputStream

    @BeforeTest
    fun setUp() {
        outputStreamCaptor = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @AfterTest
    fun tearDown() {
        System.setOut(standardOut)
        System.setIn(standardIn)
    }

    @Test
    fun `test companion object existence and properties`() {
        val gameManagerObject = GameManager::class

        // Verify that the object has the expected properties and functions
        val expectedFunctions = setOf("start", "getUserGuess", "promptRestart")
        val actualFunctions = gameManagerObject.declaredFunctions.map { it.name }.toSet()
        assertTrue(expectedFunctions.all { it in actualFunctions }, "All expected functions should be declared")

        // Check parameter sizes for each function (excluding hidden instance parameter)
        val startFunction = gameManagerObject.declaredFunctions.find { it.name == "start" }
        assertNotNull(startFunction, "start function should exist")
        assertEquals(0, startFunction.parameters.size - 1, "start should have no explicit parameters")

        val getUserGuessFunction = gameManagerObject.declaredFunctions.find { it.name == "getUserGuess" }
        assertNotNull(getUserGuessFunction, "getUserGuess function should exist")
        assertEquals(0, getUserGuessFunction.parameters.size - 1, "getUserGuess should have no explicit parameters")

        // Check return types
        assertTrue(startFunction.returnType.toString() == "kotlin.Unit", "start should return Unit")
        assertTrue(getUserGuessFunction.returnType.toString() == "kotlin.String?", "getUserGuess should return String?")
    }


}