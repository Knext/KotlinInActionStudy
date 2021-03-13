import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import kotlin.properties.Delegates
import kotlin.reflect.KProperty
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class OverrideTest {
    data class MutablePoint(var x: Int, var y:Int) {
        operator fun plus(other: MutablePoint) :MutablePoint {
            return MutablePoint(x + other.x, y + other.y)
        }

        override fun equals(obj: Any?): Boolean {
            if (obj === this) { return true} //참조가 같은지 비교
            if (obj !is MutablePoint) { return false }
            return obj.x == x && obj.y == y
        }
    }

    operator fun MutablePoint.times(scale: Double):MutablePoint {
        return MutablePoint((x*scale).toInt(), (y*scale).toInt())
    }

    operator fun MutablePoint.get(index: Int):Int {
        return when(index) {
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
        }
    }

    operator fun MutablePoint.set(index: Int, value:Int) {
        when(index) {
            0 -> x = value
            1 -> y = value
            else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
        }
    }

    @Test
    fun testOperator() {
        val p1 = MutablePoint(20,20)
        val p2 = MutablePoint(20, 40)

        assertEquals(MutablePoint(40,60), p1+p2)
        assertEquals(MutablePoint(40,40), p1*2.0)

        val list = arrayListOf(1,2)
        list += 3
        assertEquals(arrayListOf(1,2,3), list)

        val newList = list + listOf(4,5)
        assertEquals(arrayListOf(1,2,3,4,5), newList)

        assertEquals(MutablePoint(10,20), MutablePoint(10,20))
        assertNotEquals(MutablePoint(10,20), MutablePoint(5,5))
        assertNotEquals(null, MutablePoint(5,5))
        assertEquals(p1, p1)
    }

    @Test
    fun testCompare() {
        class Person(val firstName:String, val lastName:String) : Comparable<Person> {
            override fun compareTo(other: Person): Int {
                return compareValuesBy(this, other, Person::lastName, Person::firstName)
            }
        }

        val p1 = Person("AAA", "CCC")
        val p2 = Person("BBB", "DDD")
        assertTrue(p1 < p2)
    }

    @Test
    fun test_getSet() {
        val p = MutablePoint(10,20)
        assertEquals(10, p[0])
        assertEquals(20, p[1])
        var exception = assertThrows<IndexOutOfBoundsException>("Invalid Exception error"){ p[3] }
        assertEquals("Invalid coordinate 3", exception.message)

        p[0] = 1
        p[1] = 3
        assertEquals(1, p[0])
        assertEquals(3, p[1])
        val setException = assertThrows<IndexOutOfBoundsException>("Invalid Exception error"){ p[3] = 3 }
        assertEquals("Invalid coordinate 3", setException.message)
    }

    @Test
    fun test_in() {
        data class Rectangle(val uppperLeft:MutablePoint, val lowerRight:MutablePoint)
        operator fun Rectangle.contains(p:MutablePoint) : Boolean {
            return p.x in uppperLeft.x until lowerRight.x
                    && p.y in uppperLeft.y until lowerRight.y
        }

        val rects = Rectangle(MutablePoint(10,20), MutablePoint(50,50))
        assertTrue(MutablePoint(20,30) in rects)
        assertFalse(MutablePoint(5,5) in rects)
    }

    @Test
    fun lazyLoading() {
        class Email(val address:String)
        class Person(val name: String) {
            //by lazy 구문으로 lazy loading 처리 가
            val emails by lazy {loadEmails(this)}
            /*
            private var _emails: List<Email>? = null
            val emails: List<Email>
            get() {
                if (_emails == null) {
                    _emails = loadEmails(this)
                }
                return _emails!!
            }
             */
            fun loadEmails(person: Person): List<Email> {
                println("Load ${person.name} emails")
                return listOf();
            }
        }

        val p = Person("ABC")
        p.emails
        p.emails
    }



    @Test
    fun test_delegate() {
        open class PropertyChangeAware {
            protected val changeSupport = PropertyChangeSupport(this)

            fun addPropertyChangeListener(listener: PropertyChangeListener) {
                changeSupport.addPropertyChangeListener(listener)
            }

            fun removePropertyChangeListener(listener: PropertyChangeListener) {
                changeSupport.removePropertyChangeListener(listener)
            }
        }

        class Person(val name:String, age:Int, salary:Int): PropertyChangeAware() {
            private val observer = {
                    prop:KProperty<*>, oldValue:Int, newValue:Int ->
                changeSupport.firePropertyChange(prop.name, oldValue, newValue)
            }
            var age: Int by Delegates.observable(age, observer)
            var salary:Int by Delegates.observable(salary, observer)
        }

        val p = Person("ABC", 32, 2000)
        p.addPropertyChangeListener(PropertyChangeListener { event ->
            println("Property ${event.propertyName} changed" +
            "from ${event.oldValue} to ${event.newValue}")
        })

        p.age = 35
        p.salary = 2100
    }
}
