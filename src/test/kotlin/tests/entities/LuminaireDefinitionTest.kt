package tests.entities

import CreateWithXmlElement
import XmlElemental
import display.drawSvgPlanContent
import entities.Luminaire
import entities.LuminaireDefinition
import org.apache.batik.dom.GenericDOMImplementation
import org.apache.batik.svggen.SVGGraphics2D
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import org.w3c.dom.DOMImplementation
import startSvg
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertIs

class LuminaireDefinitionTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("luminaire-definition")
    xmlElement.setAttribute("name", "name")
    xmlElement.setAttribute("weight", "1.2")
    val svgElement = IIOMetadataNode("svg")
    xmlElement.appendChild(svgElement)
    return xmlElement
  }

  fun minimalLuminaireXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("luminaire")
    xmlElement.setAttribute("type", "name")
    xmlElement.setAttribute("location", "17.6")
    return xmlElement
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode("luminaire-definition")
    val svgElement = IIOMetadataNode("svg")
    xmlElement.appendChild(svgElement)

    val element = LuminaireDefinition.factory(xmlElement, null)

    assertIs<XmlElemental>(element)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<LuminaireDefinition>>(LuminaireDefinition)
  }

  @Test
  fun `companion has tag`() {
    Assertions.assertThat(LuminaireDefinition.Tag).isEqualTo("luminaire-definition")
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<LuminaireDefinition>(LuminaireDefinition.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = LuminaireDefinition.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.name).isEqualTo("name")
      assertThat(instance.weight).isEqualTo(1.2f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `registers optional attributes`() {
    val xmlElement = IIOMetadataNode("luminaire-definition")
    xmlElement.setAttribute("name", "name")
    xmlElement.setAttribute("weight", "1.2")
    xmlElement.setAttribute("complete", "1")
    xmlElement.setAttribute("width", "2.3")
    xmlElement.setAttribute("length", "3.4")
    val svgElement = IIOMetadataNode("svg")
    xmlElement.appendChild(svgElement)

    val instance = LuminaireDefinition.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.complete).isEqualTo(true)
      assertThat(instance.width).isEqualTo(2.3f)
      assertThat(instance.length).isEqualTo(3.4f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode("luminaire-definition")
    val svgElement = IIOMetadataNode("svg")
    xmlElement.appendChild(svgElement)

    val instance = LuminaireDefinition.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required name attribute",
        "Missing required weight attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode("luminaire-definition")
    xmlElement.setAttribute("name", "name")
    xmlElement.setAttribute("weight", "-1.2")
    xmlElement.setAttribute("complete", "2")
    xmlElement.setAttribute("width", "-2.3")
    xmlElement.setAttribute("length", "-3.4")
    val svgElement = IIOMetadataNode("svg")
    xmlElement.appendChild(svgElement)

    val instance = LuminaireDefinition.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read positive floating-point number from weight attribute",
        "Unable to read boolean 1 from complete attribute",
        "Unable to read positive floating-point number from width attribute",
        "Unable to read positive floating-point number from length attribute",
      )
    }.assertAll()
  }

  @Test
  fun `generates SVG when this luminaire type is in use`() {
    LuminaireDefinition.Companion.instances.clear()
     LuminaireDefinition.factory(minimalXml(), null)
     Luminaire.factory(minimalLuminaireXml(), null)

//    val domImpl: DOMImplementation = GenericDOMImplementation.getDOMImplementation()
//    val namespace = "http://www.w3.org/2000/svg"
//    val document = domImpl.createDocument(namespace, "svg", null)
//    val svgGenerator = SVGGraphics2D(document)
//    val root = svgGenerator.root
//    root.setAttribute("xmlns:plot", "http://www.davidsilber.name/namespaces/plot")

    val svgDocument = startSvg()
    drawSvgPlanContent(      svgDocument    )

//    drawSvgPlanContent(document, namespace, root)

    val defsList = svgDocument.root.getElementsByTagName("defs")
    assertThat(defsList.length).isEqualTo(2)
  }

  @Test
  fun `generates no SVG when this luminaire type is in not use`(){
    LuminaireDefinition.Companion.instances.clear()
     LuminaireDefinition.factory(minimalXml(), null)

//    val domImpl: DOMImplementation = GenericDOMImplementation.getDOMImplementation()
//    val namespace = "http://www.w3.org/2000/svg"
//    val document = domImpl.createDocument(namespace, "svg", null)
//    val svgGenerator = SVGGraphics2D(document)
//    val root = svgGenerator.root
//    root.setAttribute("xmlns:plot", "http://www.davidsilber.name/namespaces/plot")


    val svgDocument = startSvg()
    drawSvgPlanContent(      svgDocument    )

    val defsList = svgDocument.root.getElementsByTagName("defs")
    assertThat(defsList.length).isEqualTo(1)
  }

}
