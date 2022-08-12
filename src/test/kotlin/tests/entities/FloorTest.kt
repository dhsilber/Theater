package tests.entities

import CreateWithXmlElement
import HorizontalPlane
import Xml
import XmlElemental
import Startup
import entities.Floor
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

internal class FloorTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("z", "27")
    xmlElement.setAttribute("x", "0.1")
    xmlElement.setAttribute("y", "0.2")
    xmlElement.setAttribute("width", "202")
    xmlElement.setAttribute("depth", "101")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is elemental`() {
    val xmlElement = IIOMetadataNode()

    val instance = Floor.factory(xmlElement, null)

    assertIs<XmlElemental>(instance)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Floor>>(Floor)
  }

  @Test
  fun `companion has tag`() {
    assertThat(Floor.Tag).isEqualTo("floor")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Floor.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Floor>(Floor.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = Floor.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.surface).isEqualTo(HorizontalPlane(27f, 0.1F, 0.2f, 202f, 101f))
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Floor.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required z attribute",
        "Missing required x attribute",
        "Missing required y attribute",
        "Missing required width attribute",
        "Missing required depth attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("z", "zee")
    xmlElement.setAttribute("x", "bogus.1")
    xmlElement.setAttribute("y", "bogus.2")
    xmlElement.setAttribute("width", "bogus.3")
    xmlElement.setAttribute("depth", "bogus.4")

    val instance = Floor.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read floating-point number from z attribute",
        "Unable to read floating-point number from x attribute",
        "Unable to read floating-point number from y attribute",
        "Unable to read floating-point number from width attribute",
        "Unable to read floating-point number from depth attribute",
      )
    }.assertAll()
  }
}
