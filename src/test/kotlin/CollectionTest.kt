import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.StringReader
import java.lang.NumberFormatException
import java.lang.management.BufferPoolMXBean

class CollectionTest {

    @Test
    fun test_collection() {
        fun readNumbers(reader: BufferedReader) : List<Int?> {
            val result = ArrayList<Int?>()
            for (line in reader.lineSequence()) {
                result.add(line.toIntOrNull())
            }
            return result
        }

        fun addValidNumbers(numbers: List<Int?>): Int {
            //val validNumbers = numbers.filterNotNull()
            var sumValid = 0
            var invalid = 0
            for (number in numbers) {
                if (number != null) {
                    sumValid += number
                } else {
                    invalid++
                }
            }
            println("# of Invalid Numbers: $invalid")
            return sumValid
        }

        val reader = BufferedReader(StringReader("1\nabc\n42"))
        val numbers = readNumbers(reader)

        assertEquals(43, addValidNumbers(numbers))
    }

    @Test
    fun test_copy() {
        fun <T> copy(source:Collection<T>, target: MutableCollection<T>) {
            for (item in source) {
                target.add(item)
            }
        }

        val source:Collection<Int> = arrayListOf(3,4,5)
        val target:MutableCollection<Int> = arrayListOf(1)
        copy(source, target)
        println(target)

        //INFO: object 비교인데 어떻게 동작하는거지??
        assertEquals( arrayListOf(1,3,4,5), target)
    }

    @Test
    fun test_array() {
        //INFO: 26의 크기를 가진 Array를 생성하고, 'a'부터 총 26개까지 문자열을 순차적으로 생성해서 채워 넣는다.
        val letters = Array(26) {i -> ('a' + i).toString()}
        assertEquals("abcdefghijklmnopqrstuvwxyz", letters.joinToString(""))

        val squares = IntArray(5) { it*it}
        assertEquals(intArrayOf(0, 1, 4, 9, 16).joinToString(), squares.joinToString())
    }
}