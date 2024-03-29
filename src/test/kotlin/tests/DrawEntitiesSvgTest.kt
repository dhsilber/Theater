package tests

import coordinates.VenuePoint
import display.SvgBoundary
import display.drawPlan
import display.drawSvgPlan
//import display.drawSvgPlan
import entities.Floor
import entities.Luminaire
import entities.LuminaireDefinition
import entities.Pipe
import entities.PipeBase
import org.assertj.core.api.Assertions.assertThat
import startSvg
import tests.entities.FloorTest
import tests.entities.LuminaireDefinitionTest
import tests.entities.LuminaireTest
import tests.entities.PipeBaseTest
import tests.entities.PipeTest
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.fail
import kotlin.test.Test

class DrawEntitiesSvgTest {

  fun minimalLuminaireDefinitionXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("luminaire-definition")
    xmlElement.setAttribute("name", "name")
    xmlElement.setAttribute("weight", "1.2")
    xmlElement.setAttribute("length", "4.8")
    xmlElement.setAttribute("width", "3.2")
    val svgElement = IIOMetadataNode("svg")
    xmlElement.appendChild(svgElement)
    return xmlElement
  }

  fun minimalLuminaireXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("luminaire")
    xmlElement.setAttribute("type", "name")
    xmlElement.setAttribute("location", "1.6")
    return xmlElement
  }

  @Test
  fun `Luminaire drawSvgPlan uses symbol provided by LuminaireDefinition`() {
    val luminaire = Luminaire.factory(LuminaireTest().minimalXml())
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length
    assertThat(svgDocument.root.getElementsByTagName("use").length).isEqualTo(0)

    luminaire.drawSvgPlan(svgDocument, VenuePoint(0f, 0f, 0f), 0f)

    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 1)
    val useElements = svgDocument.root.getElementsByTagName("use")
    assertThat(useElements.length).isEqualTo(1)
    val xlinkAttribute = useElements.item(0).attributes.getNamedItem("xlink:href")
    assertThat(xlinkAttribute.nodeValue).isEqualTo("#Type value")
    assertThat(xlinkAttribute.firstChild.nodeValue).isEqualTo("#Type value")
  }

  @Test
  fun `Luminaire drawSvgPlan provides boundary from LuminaireDefinition`() {
    LuminaireDefinition.factory(minimalLuminaireDefinitionXml())
    val luminaire = Luminaire.factory(minimalLuminaireXml())
    val svgDocument = startSvg()
    val expectedMinX = luminaire.location - 4.8f
    val expectedMaxX = luminaire.location + 4.8f
    val expectedMinY = 15.2f
    val expectedMaxY = 24.8f

    val boundary = luminaire.drawSvgPlan(svgDocument, VenuePoint(10f, 20f, 0f), luminaire.location)

    assertThat(boundary).isEqualTo(SvgBoundary(expectedMinX, expectedMinY, expectedMaxX, expectedMaxY))
  }

  @Test
  fun `PipeBase drawSvgPlan draws pipe base`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml())
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length
    assertThat(svgDocument.root.getElementsByTagName("circle").length).isEqualTo(0)

    pipeBase.drawSvgPlan(svgDocument)

    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 1)
    val circleElements = svgDocument.root.getElementsByTagName("circle")
    assertThat(circleElements.length).isEqualTo(1)
    val radiusAttribute = circleElements.item(0).attributes.getNamedItem("r")
    assertThat(radiusAttribute.textContent.toFloat()).isEqualTo(18f)
  }

  @Test
  fun `Pipe drawSvgPlan draws empty pipe`() {
    val pipe = Pipe.factory(PipeTest().minimalXmlWithNoParent())
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length

    pipe.drawSvgPlan(svgDocument)

    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 1)
  }

  @Test
  fun `Pipe drawSvgPlan draws all of the dependents`() {
    val pipe = Pipe.factory(PipeTest().minimalXmlWithNoParent())
    val luminaire1 = Luminaire.factory(LuminaireTest().minimalXml())
    val luminaire2 = Luminaire.factory(LuminaireTest().minimalXml())
    pipe.hang(luminaire1)
    pipe.hang(luminaire2)
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length

    pipe.drawSvgPlan(svgDocument)

    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 3)
  }

  @Test
  fun `Pipe (horizontal) drawSvgPlan provides boundary`() {
    val pipe = Pipe.factory(PipeTest().minimalXmlWithNoParent())
    val svgDocument = startSvg()

    val boundry = pipe.drawSvgPlan(svgDocument)

    // See values in PipeTest().minimalXml() for justification of these numbers
    assertThat(boundry).isEqualTo(SvgBoundary(1.2f, 2.3f, 5.7f, 4.3f))
  }

  @Test
  fun `Pipe (vertical) drawSvgPlan provides boundary`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)
    val verticalPipe = Pipe.factory(PipeTest().minimalXmlWithPipeBaseParent(), pipeBase)
    val svgDocument = startSvg()

    val boundry = verticalPipe.drawSvgPlan(svgDocument)

    assertThat(boundry).isEqualTo(SvgBoundary(-0.9f, -0.8f, 1.1f, 1.2f))
  }

  @Test
  fun `Pipe drawSvgPlan provides boundary that includes Luminaires`() {
    val pipe = Pipe.factory(PipeTest().minimalXmlWithNoParent())
    LuminaireDefinition.factory(minimalLuminaireDefinitionXml())
    val luminaire = Luminaire.factory(LuminaireDefinitionTest().minimalLuminaireXml())
    pipe.hang(luminaire)
    val svgDocument = startSvg()
    val expectedMinX = pipe.origin.venue.x
    val expectedMaxX = pipe.origin.venue.x + pipe.length
    val expectedMinY = pipe.origin.venue.y
    val expectedMaxY = pipe.origin.venue.y + Pipe.Diameter

    val result = pipe.drawSvgPlan(svgDocument)

    assertThat(result).isEqualTo(SvgBoundary(expectedMinX, expectedMinY, expectedMaxX, expectedMaxY))

    fail("Add Luminaires to this test.")
  }
}
