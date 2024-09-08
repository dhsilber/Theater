package tests.entities

import CreateWithXmlElement
import Startup
import TagRegistry
import Xml
import XmlElemental
import entities.DeviceTemplate
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

class DeviceTemplateTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("device-template")
    xmlElement.setAttribute("type", "type name")
    xmlElement.setAttribute("width", "1.2")
    xmlElement.setAttribute("depth", "3.4")
    xmlElement.setAttribute("height", "5.6")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode("device-template")
    val svgElement = IIOMetadataNode("svg")
    xmlElement.appendChild(svgElement)

    val element = DeviceTemplate.factory(xmlElement, null)

    assertIs<XmlElemental>(element)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<DeviceTemplate>>(DeviceTemplate)
  }

  @Test
  fun `companion has tag`() {
    Assertions.assertThat(DeviceTemplate.Tag).isEqualTo("device-template")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    Assertions.assertThat(TagRegistry.tagToCallback).containsKey(DeviceTemplate.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<DeviceTemplate>(DeviceTemplate.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = DeviceTemplate.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.type).isEqualTo("type name")
      assertThat(instance.width).isEqualTo(1.2f)
      assertThat(instance.depth).isEqualTo(3.4f)
      assertThat(instance.height).isEqualTo(5.6f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `registers optional attributes`() {
    val xmlElement = minimalXml()
    xmlElement.setAttribute("layer", "layer name")
    xmlElement.setAttribute("color", "color name")

    val instance = DeviceTemplate.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.layer).isEqualTo("layer name")
      assertThat(instance.color).isEqualTo("color name")
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode("device-template")
    val svgElement = IIOMetadataNode("svg")
    xmlElement.appendChild(svgElement)

    val instance = DeviceTemplate.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required type attribute",
        "Missing required width attribute",
        "Missing required depth attribute",
        "Missing required height attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode("device-template")
    xmlElement.setAttribute("type", "type name")
    xmlElement.setAttribute("width", "-1.2")
    xmlElement.setAttribute("depth", "-2.3")
    xmlElement.setAttribute("height", "-3.4")

    val instance = DeviceTemplate.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read positive floating-point number from width attribute",
        "Unable to read positive floating-point number from depth attribute",
        "Unable to read positive floating-point number from height attribute",
      )
    }.assertAll()
  }

}
