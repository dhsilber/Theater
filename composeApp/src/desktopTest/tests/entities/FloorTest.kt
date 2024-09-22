package tests.entities

import CreateWithXmlElement
import Surface
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
    val xmlElement = IIOMetadataNode("floor")
    xmlElement.setAttribute("x", "0.1")
    xmlElement.setAttribute("y", "0.2")
    xmlElement.setAttribute("z", "27")
    xmlElement.setAttribute("width", "202")
    xmlElement.setAttribute("depth", "101")
    return xmlElement
  }

  fun minimalSlopedXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("floor")
    xmlElement.setAttribute("x", "0.1")
    xmlElement.setAttribute("y", "0.2")
    xmlElement.setAttribute("z", "27")
    xmlElement.setAttribute("width", "202")
    xmlElement.setAttribute("depth", "101")
    xmlElement.setAttribute("z-depth", "2")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is elemental`() {
    val xmlElement = IIOMetadataNode("floor")

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
      assertThat(instance.surface).isEqualTo(Surface(
        x = 0.1F,
        y = 0.2f,
        z = 27f,
        width = 202f,
        depth = 101f,
      ))
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `has required sloped attributes`() {
    val instance = Floor.factory(minimalSlopedXml(), null)

    SoftAssertions().apply {
      assertThat(instance.surface).isEqualTo(Surface(
        x = 0.1F,
        y = 0.2f,
        z = 27f,
        zDepth = 2f,
        width = 202f,
        depth = 101f
      ))
      assertThat(instance.hasError).isFalse
      assertThat(instance.errors).containsExactly()
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode("floor")

    val instance = Floor.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "floor missing required x attribute",
        "floor missing required y attribute",
        "floor missing required z attribute",
        "floor missing required width attribute",
        "floor missing required depth attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode("floor")
    xmlElement.setAttribute("x", "bogus.1")
    xmlElement.setAttribute("y", "bogus.2")
    xmlElement.setAttribute("z", "zee")
    xmlElement.setAttribute("width", "bogus.3")
    xmlElement.setAttribute("depth", "bogus.4")

    val instance = Floor.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "floor unable to read floating-point number from x attribute",
        "floor unable to read floating-point number from y attribute",
        "floor unable to read floating-point number from z attribute",
        "floor unable to read floating-point number from width attribute",
        "floor unable to read floating-point number from depth attribute",
      )
    }.assertAll()
  }
}
