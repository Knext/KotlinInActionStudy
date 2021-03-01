import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class NullTypeTest {
    @Test
    fun test_null() {
        fun strLenSafe(s:String?) :Int = /*if (s!= null) s.length else 0*/ s?.length ?: 0

        val x:String? = null
        assertEquals(0, strLenSafe(x))
    }

    @Test
    fun test_nullable() {
        fun printAllCaps(s: String?) {
            val allCaps: String? = s?.toUpperCase()
            println(allCaps)
        }
        assertEquals(kotlin.Unit, printAllCaps(null))
    }

    @Test
    fun test_class() {
        class Employee(val name:String, val manager: Employee?)
        fun managerName(employee: Employee) :String? = employee.manager?.name

        val ceo = Employee("Boss", null)
        val developer = Employee("Developer", ceo)
        assertEquals("Boss",managerName(developer))
        assertEquals(null, managerName(ceo))
    }

    @Test
    fun test_chain() {
        class Address(val streetAddress: String, val zipCode: Int, val city:String, val country:String)
        class Company(val name:String, val address:Address?)
        class Person(val name:String, val company: Company?)

        val unknown = "Unknown"
        fun Person.countryName():String {
            return company?.address?.country ?: unknown
        }

        val person = Person("Person", null)
        assertEquals(unknown, person.countryName())
    }

    @Test
    fun test_elvis() {
        fun strLenSafe(s:String?) :Int = s?.length ?: 0
        assertEquals(3, strLenSafe("abc"))
        assertEquals(0, strLenSafe(null))
    }
}