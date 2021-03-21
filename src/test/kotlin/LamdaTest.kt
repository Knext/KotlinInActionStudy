import org.junit.jupiter.api.Test
import java.lang.StringBuilder
import kotlin.test.assertEquals

internal class LamdaTest {
    @Test
    fun test() {
        fun performReq(url:String, callback: (code:Int, content:String)-> Unit) {
            callback(0, "content")
        }

        val url = "http:kotl.in"
        performReq(url) {code, content -> println("$code +  $content")}
        performReq(url) {code, body -> println("$code +  $body")}

        fun String.filter(predicate: (Char) -> Boolean):String {
            val sb = StringBuilder()
            for (index in 0 until length) {
                val element = get(index)
                if (predicate(element)) {
                    sb.append(element)
                }
            }
            return sb.toString()
        }

        println("ab1c".filter{it in 'a'..'z'})
    }

    @Test
    fun testJoinString() {
        fun <T> Collection<T>.joinToString(
            separator:String = ", ",
            prefix:String = "",
            postfix:String = "",
            transform: ((T) -> String)? = null): String {

            val result = StringBuilder(prefix)

            for ((index, element) in this.withIndex()) {
                if (index> 0) {
                    result.append(separator)
                }
                val str = transform?.invoke(element) ?: element.toString()
                result.append(str)
            }

            result.append(postfix)

            return result.toString()
        }

        val letters = listOf("Alpha", "Beta")
        assertEquals("Alpha, Beta", letters.joinToString())
        assertEquals("alpha, beta", letters.joinToString {it.toLowerCase()})
        assertEquals("ALPHA! BETA! ", letters.joinToString(separator = "! ", postfix = "! ") {it.toUpperCase()})
    }

    enum class Delivery {STANDARD, EXPEDITED}

    @Test
    fun test_return_fun() {
        class Order(val itemCount:Int)
        fun getShipCal(delivery:Delivery) : (Order) -> Double {
            if (delivery == Delivery.EXPEDITED)  {
                return {order -> 6 + 2.1 * order.itemCount}
            }
            return  { order -> 1.2 * order.itemCount}

        }

        val expeditedCal = getShipCal(Delivery.EXPEDITED)
        val standardCal = getShipCal(Delivery.STANDARD)
        assertEquals(27.0, expeditedCal(Order(10)))
        assertEquals(12.0, standardCal(Order(10)))
    }
}
