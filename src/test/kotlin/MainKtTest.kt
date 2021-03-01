import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MainKtTest {

    @Test
    fun test_alphabet() {
        val expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n" +
                "Now I know the alphabet!"
        assertEquals(expected, alphabet())
        assertEquals(expected, alphabet2())
        assertEquals(expected, alphabet3())
        assertEquals(expected, alphabet4())
        assertEquals(expected, alphabet5())
    }

    fun alphabet5() = buildString {
        for (letter in 'A'..'Z') {
            append(letter)
        }

        append("\nNow I know the alphabet!")
    }

    fun alphabet4() = StringBuilder().apply {
        for (letter in 'A'..'Z') {
            this.append(letter)
        }

        this.append("\nNow I know the alphabet!")
    }.toString()

    fun alphabet3() = with(StringBuilder()) {
        for (letter in 'A'..'Z') {
            this.append(letter)
        }

        this.append("\nNow I know the alphabet!")
        this.toString()
    }

    fun alphabet2() : String {
        val stringBuilder = StringBuilder()
        return with(stringBuilder) {
            for (letter in 'A'..'Z') {
                this.append(letter)
            }

            this.append("\nNow I know the alphabet!")
            this.toString()
        }
    }

    fun alphabet() : String {
        val stringBuilder = StringBuilder()
        for (letter in 'A'..'Z') {
            stringBuilder.append(letter)
        }

        stringBuilder.append("\nNow I know the alphabet!")
        return stringBuilder.toString()
    }
}