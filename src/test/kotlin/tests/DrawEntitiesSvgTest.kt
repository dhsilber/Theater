package tests

import coordinates.VenuePoint
import display.SvgBoundary
import display.drawSvg
import entities.Luminaire
import entities.LuminaireDefinition
import entities.Pipe
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import startSvg
import tests.entities.LuminaireDefinitionTest
import tests.entities.LuminaireTest
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

  @Test
  fun `Luminaire drawSvg uses symbol provided by LuminaireDefinition`() {
    val luminaire = Luminaire.factory(LuminaireTest().minimalXml())
    val svgDocument = startSvg()
    val initialNodeCount = svgDocument.root.childNodes.length

    luminaire.drawSvg(svgDocument, VenuePoint(0f, 0f, 0f), 0f)

    val useElements = svgDocument.root.getElementsByTagName("use")
    val xlinkAttribute = useElements.item(0).attributes.getNamedItem("xlink:href")
    assertThat(xlinkAttribute.nodeValue).isEqualTo("#Type value")
    assertThat(xlinkAttribute.firstChild.nodeValue).isEqualTo("#Type value")
  }

  @Test
  fun `Luminaire drawSvg provides boundary from LuminaireDefinition`() {
    val luminaireDefinition = LuminaireDefinition.factory(minimalLuminaireDefinitionXml())
    val luminaire = Luminaire.factory(LuminaireDefinitionTest().minimalLuminaireXml())
    val svgDocument = startSvg()

    val boundary =     luminaire.drawSvg(svgDocument, VenuePoint(10f, 20f, 0f), 17.6f)

    assertThat(boundary).isEqualTo(SvgBoundary(12.8f, 15.2f, 22.4f, 24.8f))

    fail()
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
    val svgDocument = startSvg()

    val result = pipe.drawSvg(svgDocument)

    // See values in PipeTest().minimalXml() for justification of these numbers
    assertThat(result).isEqualTo(SvgBoundary(1.2f, 2.3f, 5.7f, 4.3f))

    fail("Add Luminaires to this test.")
  }
}
