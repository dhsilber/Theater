package tests.coordinates

import coordinates.StagePoint
import entities.Proscenium
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.Test
import kotlin.test.assertEquals

class StagePointTest {
  var x = 1.0f
  var y = 2.0f
  var z = 3.0f
  val prosceniumX = 1.2f
  val prosceniumY = 2.3f
  val prosceniumZ = 3.4f

  @Test
  fun storesCoordinates() {
    Proscenium.instances.clear()
    val stagePoint = StagePoint(x, y, z)
    SoftAssertions().apply {
      assertThat(stagePoint.initial.x).isEqualTo(x)
      assertThat(stagePoint.initial.y).isEqualTo(y)
      assertThat(stagePoint.initial.z).isEqualTo(z)
      assertThat(stagePoint.venue.x).isEqualTo(x)
      assertThat(stagePoint.venue.y).isEqualTo(y)
      assertThat(stagePoint.venue.z).isEqualTo(z)
    }.assertAll()
  }

  @Test
  fun storesCoordinatesWithProscenium() {
    Proscenium.instances.clear()
    val prosceniumElement = IIOMetadataNode("proscenium")
    prosceniumElement.setAttribute("x", prosceniumX.toString())
    prosceniumElement.setAttribute("y", prosceniumY.toString())
    prosceniumElement.setAttribute("z", prosceniumZ.toString())
    prosceniumElement.setAttribute("height", "4.5")
    prosceniumElement.setAttribute("width", "5.6")
    prosceniumElement.setAttribute("depth", "6.7")
    assertEquals(false, Proscenium.inUse() )

    Proscenium.factory(prosceniumElement, null)
    val stagePoint = StagePoint(x, y, z)
    SoftAssertions().apply {
      assertThat(stagePoint.initial.x).isEqualTo(x)
      assertThat(stagePoint.initial.y).isEqualTo(y)
      assertThat(stagePoint.initial.z).isEqualTo(z)
      assertThat(stagePoint.venue.x).isEqualTo( prosceniumX + x)
      assertThat(stagePoint.venue.y).isEqualTo( prosceniumY - y)
      assertThat(stagePoint.venue.z).isEqualTo( prosceniumZ + z)
    }.assertAll()
  }

}
