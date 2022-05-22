package tests

import PointOffset
import org.assertj.core.api.SoftAssertions
import kotlin.test.Test

class PointOffsetTest {
  var x = 1.0f
  var y = 2.0f
  var z = 3.0f

  @Test
  fun storesCoordinates() {
    val offsetPoint = PointOffset(x, y, z)
    SoftAssertions().apply {
      assertThat(offsetPoint.x).isEqualTo(x)
      assertThat(offsetPoint.y).isEqualTo(y)
      assertThat(offsetPoint.z).isEqualTo(z)
    }.assertAll()
  }

}
