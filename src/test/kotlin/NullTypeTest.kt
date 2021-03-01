import org.junit.jupiter.api.Test

internal class NullTypeTest {
    @Test
    fun test_null() {
        fun strLenSafe(s:String?) :Int = /*if (s!= null) s.length else 0*/ s?.length ?: 0

        val x:String? = null
        println(strLenSafe(x))
    }

    @Test
    fun test_nullable() {
        fun printAllCaps(s: String?) {
            val allCaps: String? = s?.toUpperCase()
            println(allCaps)
        }
        printAllCaps(null)
    }

    @Test
    fun test_class() {
        class Employee(val name:String, val manager: Employee?)
        fun managerName(employee: Employee) :String? = employee.manager?.name

        val ceo = Employee("Boss", null)
        val developer = Employee("Developer", ceo)
        println(managerName(developer))
        println(managerName(ceo))
    }

    @Test
    fun test_chain() {
        class Address(val streetAddress: String, val zipCode: Int, val city:String, val country:String)
        class Company(val name:String, val address:Address?)
        class Person(val name:String, val company: Company?)

        fun Person.countryName():String {
            return company?.address?.country ?: "Unknown"
        }
        val person = Person("Person", null)
        println(person.countryName())
    }


}