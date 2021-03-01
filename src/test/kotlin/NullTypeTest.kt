import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
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

    @Test
    fun test_elvis2() {
        class Address(val streetAddress:String, val zipCode:Int, val city:String, val country:String)
        class Company(val name:String, val address:Address?)
        class Person(val name:String, val company:Company?)
        fun printShippingLabel(person:Person) {
            val address = person.company?.address ?: throw IllegalArgumentException("No Address")
            with(address) {
                println(streetAddress)
                println("$zipCode $city $country")
            }
        }

        val address = Address("Ara", 1234, "Seoul", "Korea")
        val company = Company("JJ", address)
        val person = Person("Someone", company)
        printShippingLabel(person)
        assertThrows(IllegalArgumentException::class.java) {
            printShippingLabel(Person("Exception", null))
        }
    }

    @Test
    fun test_let() {
        fun sendEmailTo(email:String) {
            println("Sendind Email to $email")
        }

        var email:String? = "aaa@abc.com"
        email?.let{sendEmailTo(it)}

        email = null
        email?.let{sendEmailTo(it)}
    }

    @Test
    fun test_initialize() {
        class Service(){
            fun actionValue(): String = "foo"
        }
        class MyService() {
            fun action():String {
                service = Service() /*초기화를 수행하지 않으면 lateinit property exception 발생*/
                return service.actionValue()
            }
            private lateinit var service: Service
        }

        val myService = MyService()
        assertEquals("foo" , myService.action())
    }

    @Test
    fun test_isNullOrBlank() {
        fun verify(input:String?):String {
            //return if (input.isNullOrBlank()) "input is blank" else input
            if (input.isNullOrBlank()) {
                return "input is blank"
            }
            return input
        }

        assertEquals("input is blank", verify(null))
        assertEquals("input is blank", verify(" "))
        assertEquals("test input", verify("test input"))
    }
}