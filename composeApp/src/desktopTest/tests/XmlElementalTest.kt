package tests

import coordinates.Point
import coordinates.PointOffset
import Rectangle
import coordinates.VenuePoint
import XmlElemental
import org.assertj.core.api.Assertions.assertThat
import org.w3c.dom.Element
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.Test

class XmlElementalTest {
  private val venueX = 5.5f
  private val venueY = 6.6f
  private val stageX = 5.7f
  private val stageY = 9.7f
  private val offsetX = 1.1f
  private val offsetY = 2.2f
  private val offsetZ = 3.3f
  private val rectangleList = "3.4 4.5"

  class GenericEntity(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
    var venuePoint = getVenuePointAttribute("venue-x", "venue-y")
    var stagePoint = getStagePointAttribute("stage-x", "stage-y")
//    val pointOffsetXYZ = getPointOffsetAttribute("offset-x", "offset-y", "offset-z")
//    val pointOffsetXY = getPointOffsetAttribute("offset-x", "offset-y")
    val rectangle = getRectangleAttribute("rectangle")
  }

  class BadRectangleEntity(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
    init {
      getRectangleAttribute("data")
    }
  }

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("element")
    xmlElement.setAttribute("venue-x", venueX.toString())
    xmlElement.setAttribute("venue-y", venueY.toString())
    xmlElement.setAttribute("stage-x", stageX.toString())
    xmlElement.setAttribute("stage-y", stageY.toString())
    xmlElement.setAttribute("offset-x", offsetX.toString())
    xmlElement.setAttribute("offset-y", offsetY.toString())
    xmlElement.setAttribute("offset-z", offsetZ.toString())
    xmlElement.setAttribute("rectangle", rectangleList)
    return xmlElement
  }

  @Test
  fun `getVenuePointAttribute gets VenuePoint at zero altitude`() {
    val generic = GenericEntity(minimalXml(), null)
    assertThat(generic.venuePoint).isEqualTo(VenuePoint(venueX, venueY, 0f))
  }

  @Test
  fun `getStagePointAttribute gets VenuePoint at zero altitude`() {
    val generic = GenericEntity(minimalXml(), null)
    assertThat(generic.stagePoint.initial).isEqualTo(Point(stageX, stageY, 0f))
  }

//  @Test
//  fun `getPointOffsetAttribute gets PointOffset at zero altitude`() {
//    val generic = GenericEntity(minimalXml(), null)
//    assertThat(generic.pointOffsetXY).isEqualTo(PointOffset(offsetX, offsetY, 0f))
//  }
//
//  @Test
//  fun `getPointOffsetAttribute gets PointOffset `() {
//    val generic = GenericEntity(minimalXml(), null)
//    assertThat(generic.pointOffsetXYZ).isEqualTo(PointOffset(offsetX, offsetY, offsetZ))
//  }

  @Test
  fun `getRectangleAttribute`() {
    val generic = GenericEntity(minimalXml(), null)
    assertThat(generic.rectangle).isEqualTo(Rectangle( 3.4f, 4.5f))
  }

  @Test
  fun `getRectangleAttribute sets error for invalid data`() {
    val xmlElement = IIOMetadataNode("element")
    val text = "Not good rectangle data"
    xmlElement.setAttribute("data", text)

    val badRectangleEntity = BadRectangleEntity(xmlElement, null)

    assertThat(badRectangleEntity.errors).containsExactly(
      "element unable to read rectangle specification from data attribute containing '$text'",
    )
  }

  @Test
  fun `getRectangleAttribute sets error for not enough data`() {
    val xmlElement = IIOMetadataNode("element")
    val text = "5.6"
    xmlElement.setAttribute("data", text)

    val badRectangleEntity = BadRectangleEntity(xmlElement, null)

    assertThat(badRectangleEntity.errors).containsExactly(
      "element unable to read rectangle specification from data attribute containing '$text'",
    )
  }

  @Test
  fun `getRectangleAttribute sets error for missing data`() {
    val xmlElement = IIOMetadataNode("element")

    val badRectangleEntity = BadRectangleEntity(xmlElement, null)

    assertThat(badRectangleEntity.errors).containsExactly(
      "element missing required data attribute",
    )
  }

}
