package tests.coordinates

import coordinates.VenuePoint
import org.assertj.core.api.SoftAssertions
import kotlin.test.Test

class VenuePointTest {
  var x = 1.0f
  var y = 2.0f
  var z = 3.0f

  @Test
  fun storesCoordinates() {
    val venuePoint = VenuePoint(x, y, z)
    SoftAssertions().apply {
      assertThat(venuePoint.x).isEqualTo(x)
      assertThat(venuePoint.y).isEqualTo(y)
      assertThat(venuePoint.z).isEqualTo(z)
    }.assertAll()
  }

  @Test
  fun storesCoordinatesInteger() {
    val venuePoint = VenuePoint(x.toInt(), y.toInt(), z.toInt())
    SoftAssertions().apply {
      assertThat(venuePoint.x).isEqualTo(x)
      assertThat(venuePoint.y).isEqualTo(y)
      assertThat(venuePoint.z).isEqualTo(z)
    }.assertAll()
  }

  @Test
  fun `tracks extreme values used`() {
    VenuePoint.SmallX = Float.MAX_VALUE
    VenuePoint.SmallY = Float.MAX_VALUE
    VenuePoint.SmallZ = Float.MAX_VALUE
    VenuePoint.LargeX = Float.MIN_VALUE
    VenuePoint.LargeY = Float.MIN_VALUE
    VenuePoint.LargeZ = Float.MIN_VALUE
    VenuePoint(x, y, z)
    SoftAssertions().apply {
      assertThat(VenuePoint.SmallX).isEqualTo(x)
      assertThat(VenuePoint.SmallY).isEqualTo(y)
      assertThat(VenuePoint.SmallZ).isEqualTo(z)
      assertThat(VenuePoint.LargeX).isEqualTo(x)
      assertThat(VenuePoint.LargeY).isEqualTo(y)
      assertThat(VenuePoint.LargeZ).isEqualTo(z)
    }.assertAll()
  }

}
