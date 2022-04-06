package tests

import Point
import org.junit.Test
import kotlin.test.assertEquals


/**
 * @author dhs
 * @since 0.0.2
 */
class PointTest {
  var x = 1.0f
  var y = 2.0f
  var z = 3.0f

  @Test
  fun storesCoordinates() {
    val point = Point(x, y, z)
    assertEquals(point.x, x)
    assertEquals(point.y, y)
    assertEquals(point.z, z)
  }

  @Test
  fun storesCoordinatesInteger() {
    val point = Point(x.toInt(), y.toInt(), z.toInt())
    assertEquals(point.x, x)
    assertEquals(point.y, y)
    assertEquals(point.z, z)
  }

}
