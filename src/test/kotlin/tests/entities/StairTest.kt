package tests.entities

import CreateWithXmlElement
import TagRegistry
import Xml
import XmlElemental
import Startup
import coordinates.StagePoint
import coordinates.VenuePoint
import entities.Pipe
import entities.Stair
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

internal class StairTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("stair")
    xmlElement.setAttribute("x", "11")
    xmlElement.setAttribute("y", "12")
    xmlElement.setAttribute("z", "3")
    xmlElement.setAttribute("width", "34")
    xmlElement.setAttribute("steps", "2")
    xmlElement.setAttribute("run", "10")
    xmlElement.setAttribute("rise", "7")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is elemental`() {
    val xmlElement = IIOMetadataNode("stair")

    val instance = Stair.factory(xmlElement, null)

    assertIs<XmlElemental>(instance)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Stair>>(Stair)
  }

  @Test
  fun `companion has tag`() {
    assertThat(Stair.Tag).isEqualTo("stair")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Stair.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Stair>(Stair.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = Stair.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.origin).isEqualTo(VenuePoint(11F, 12f, 3f))
      assertThat(instance.width).isEqualTo(34f)
      assertThat(instance.steps).isEqualTo(2)
      assertThat(instance.run).isEqualTo(10f)
      assertThat(instance.rise).isEqualTo(7f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode("stair")

    val instance = Stair.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "stair missing required x attribute",
        "stair missing required y attribute",
        "stair missing required z attribute",
        "stair missing required width attribute",
        "stair missing required steps attribute",
        "stair missing required run attribute",
        "stair missing required rise attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode("stair")
    xmlElement.setAttribute("x", "bogus.1")
    xmlElement.setAttribute("y", "bogus.2")
    xmlElement.setAttribute("z", "zee")
    xmlElement.setAttribute("width", "bogus.4")
    xmlElement.setAttribute("steps", "bogus.5")
    xmlElement.setAttribute("run", "bogus.6")
    xmlElement.setAttribute("rise", "bogus.7")

    val instance = Stair.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "stair unable to read floating-point number from x attribute",
        "stair unable to read floating-point number from y attribute",
        "stair unable to read floating-point number from z attribute",
        "stair unable to read floating-point number from width attribute",
        "stair unable to read positive integer from steps attribute",
        "stair unable to read floating-point number from run attribute",
        "stair unable to read floating-point number from rise attribute",
      )
    }.assertAll()
  }

}
