package tests

import coordinates.VenuePoint
import display.SvgBoundary
import display.drawSvg
import entities.Luminaire
import entities.LuminaireDefinition
import entities.Pipe
import entities.PipeBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import startSvg
import tests.entities.LuminaireDefinitionTest
import tests.entities.LuminaireTest
import tests.entities.PipeBaseTest
import tests.entities.PipeTest
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.fail

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
  fun `Luminaire drawSvg uses symbol provided by LuminaireDefinition`() {
    val luminaire = Luminaire.factory(LuminaireTest().minimalXml())
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length
    assertThat(svgDocument.root.getElementsByTagName("use").length).isEqualTo(0)

    luminaire.drawSvg(svgDocument, VenuePoint(0f, 0f, 0f), 0f)

    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 1)
    val useElements = svgDocument.root.getElementsByTagName("use")
    assertThat(useElements.length).isEqualTo(1)
    val xlinkAttribute = useElements.item(0).attributes.getNamedItem("xlink:href")
    assertThat(xlinkAttribute.nodeValue).isEqualTo("#Type value")
    assertThat(xlinkAttribute.firstChild.nodeValue).isEqualTo("#Type value")
  }

  @Test
  fun `Luminaire drawSvg provides boundary from LuminaireDefinition`() {
    LuminaireDefinition.factory(minimalLuminaireDefinitionXml())
    val luminaire = Luminaire.factory(minimalLuminaireXml())
    val svgDocument = startSvg()
    val expectedMinX = luminaire.location - 4.8f
    val expectedMaxX = luminaire.location + 4.8f
    val expectedMinY = 15.2f
    val expectedMaxY = 24.8f

    val boundary = luminaire.drawSvg(svgDocument, VenuePoint(10f, 20f, 0f), luminaire.location)

    assertThat(boundary).isEqualTo(SvgBoundary(expectedMinX, expectedMinY, expectedMaxX, expectedMaxY))
  }

  @Test
  fun `PipeBase drawSvg draws pipe base`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml())
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length
    assertThat(svgDocument.root.getElementsByTagName("circle").length).isEqualTo(0)

    pipeBase.drawSvg(svgDocument)

    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 1)
    val circleElements = svgDocument.root.getElementsByTagName("circle")
    assertThat(circleElements.length).isEqualTo(1)
    val result = circleElements.item(0)
    assertThat(result.attributes.getNamedItem("r")).isEqualTo("18")

  }

  @Test
  fun `Pipe drawSvg draws empty pipe`() {
    val pipe = Pipe.factory(PipeTest().minimalXml())
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length

    pipe.drawSvg(svgDocument)

    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 1)
  }

  @Test
  fun `Pipe drawSvg draws all of the dependents`() {
    val pipe = Pipe.factory(PipeTest().minimalXml())
    val luminaire1 = Luminaire.factory(LuminaireTest().minimalXml())
    val luminaire2 = Luminaire.factory(LuminaireTest().minimalXml())
    pipe.hang(luminaire1)
    pipe.hang(luminaire2)
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length

    pipe.drawSvg(svgDocument)

    assertThat(svgDocument.root.childNodes.length).isEqualTo(initialNodeCount + 3)
  }

  @Test
  fun `Pipe drawSvg provides boundary`() {
    val pipe = Pipe.factory(PipeTest().minimalXml())
    val svgDocument = startSvg()

    val result = pipe.drawSvg(svgDocument)

    // See values in PipeTest().minimalXml() for justification of these numbers
    assertThat(result).isEqualTo(SvgBoundary(1.2f, 2.3f, 5.7f, 4.3f))
  }

  @Test
  fun `Pipe drawSvg provides boundary that includes Luminaires`() {
    val pipe = Pipe.factory(PipeTest().minimalXml())
    LuminaireDefinition.factory(minimalLuminaireDefinitionXml())
    val luminaire = Luminaire.factory(LuminaireDefinitionTest().minimalLuminaireXml())
    pipe.hang(luminaire)
    val svgDocument = startSvg()
    val expectedMinX = pipe.origin.venue.x
    val expectedMaxX = pipe.origin.venue.x + pipe.length
    val expectedMinY = pipe.origin.venue.y
    val expectedMaxY = pipe.origin.venue.y + Pipe.Diameter

    val result = pipe.drawSvg(svgDocument)

    assertThat(result).isEqualTo(SvgBoundary(expectedMinX, expectedMinY, expectedMaxX, expectedMaxY))

    fail("Add Luminaires to this test.")
  }
}
