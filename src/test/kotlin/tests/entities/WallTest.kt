package tests.entities

import CreateWithXmlElement
import TagRegistry
import Xml
import XmlElemental
import Startup
import coordinates.VenuePoint
import entities.Wall
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

class WallTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("wall")
    xmlElement.setAttribute("x1", "0.1")
    xmlElement.setAttribute("y1", "0.2")
    xmlElement.setAttribute("x2", "0.3")
    xmlElement.setAttribute("y2", "0.4")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is elemental`() {
    val xmlElement = IIOMetadataNode("wall")

    val wall = Wall.factory(xmlElement, null)

    assertIs<XmlElemental>(wall)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Wall>>(Wall)
  }

  @Test
  fun `companion has tag`() {
    assertThat(Wall.Tag).isEqualTo("wall")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Wall.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Wall>(Wall.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = Wall.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.start).isEqualTo(VenuePoint(0.1F, 0.2f, 0f))
      assertThat(instance.end).isEqualTo(VenuePoint(0.3F, 0.4f, 0f))
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode("wall")

    val instance = Wall.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "wall missing required x1 attribute",
        "wall missing required y1 attribute",
        "wall missing required x2 attribute",
        "wall missing required y2 attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode("wall")
    xmlElement.setAttribute("x1", "bogus.1")
    xmlElement.setAttribute("y1", "bogus.2")
    xmlElement.setAttribute("x2", "bogus.3")
    xmlElement.setAttribute("y2", "bogus.4")

    val instance = Wall.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "wall unable to read floating-point number from x1 attribute",
        "wall unable to read floating-point number from y1 attribute",
        "wall unable to read floating-point number from x2 attribute",
        "wall unable to read floating-point number from y2 attribute",
      )
    }.assertAll()
  }
}
