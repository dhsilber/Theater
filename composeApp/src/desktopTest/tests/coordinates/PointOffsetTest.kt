package tests.coordinates

import coordinates.PointOffset
import entities.Proscenium
import org.assertj.core.api.SoftAssertions
import kotlin.test.Test

class PointOffsetTest {
  var x = 1.0f
  var y = 2.0f
  var z = 3.0f

  @Test
  fun storesCoordinates() {
    Proscenium.Companion.instances.clear()

    val offsetPoint = PointOffset(x, y, z)
    
    SoftAssertions().apply {
      assertThat(offsetPoint.x).isEqualTo(x)
      assertThat(offsetPoint.y).isEqualTo(y)
      assertThat(offsetPoint.z).isEqualTo(z)
    }.assertAll()
  }

}
