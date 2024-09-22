package tests.entities

import CreateWithXmlElement
import TagRegistry
import Xml
import XmlElemental
import Startup
import display.drawSvgPlanContent
import entities.Luminaire
import entities.LuminaireDefinition
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import startSvg
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
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

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
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
    assertThat(LuminaireDefinition.Tag).isEqualTo("luminaire-definition")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(LuminaireDefinition.Tag)
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
        "luminaire-definition missing required name attribute",
        "luminaire-definition missing required weight attribute",
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
        "luminaire-definition unable to read positive floating-point number from weight attribute",
        "luminaire-definition unable to read boolean 1 from complete attribute",
        "luminaire-definition unable to read positive floating-point number from width attribute",
        "luminaire-definition unable to read positive floating-point number from length attribute",
      )
    }.assertAll()
  }

  @Test
  fun `generates SVG when this luminaire type is in use`() {
    LuminaireDefinition.Companion.instances.clear()
    LuminaireDefinition.factory(minimalXml(), null)
    Luminaire.factory(minimalLuminaireXml(), null)
    val svgDocument = startSvg()
    val initialSymbolElementCount = svgDocument.root.getElementsByTagName("symbol").length

    drawSvgPlanContent(svgDocument)

    val symbolElements = svgDocument.root.getElementsByTagName("symbol")
    assertThat(symbolElements.length).isEqualTo(initialSymbolElementCount + 1)
  }

  @Test
  fun `generates no SVG when this luminaire type is in not use`() {
    LuminaireDefinition.Companion.instances.clear()
    LuminaireDefinition.factory(minimalXml(), null)
    val svgDocument = startSvg()

    drawSvgPlanContent(svgDocument)

    val symbolElements = svgDocument.root.getElementsByTagName("symbol")
    assertThat(symbolElements.length).isEqualTo(1)
  }

}
