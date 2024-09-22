package tests.entities

import CreateWithXmlElement
import TagRegistry
import Xml
import XmlElemental
import Startup
import entities.Venue
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class VenueTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("venue")
    xmlElement.setAttribute("building", "Building value")
    xmlElement.setAttribute("room", "Room value")
    xmlElement.setAttribute("width", "12")
    xmlElement.setAttribute("depth", "23")
    xmlElement.setAttribute("height", "34")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode("venue")

    val luminaire = Venue.factory(xmlElement, null)

    assertIs<XmlElemental>(luminaire)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Venue>>(Venue)
  }

  @Test
  fun `companion has tag`() {
    assertEquals("venue", Venue.Tag )
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Venue.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Venue>(Venue.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = Venue.factory(minimalXml(), null)

    assertEquals("Building value", instance.building)
    assertEquals("Room value", instance.room)
    assertEquals(12, instance.width)
    assertEquals(23, instance.depth)
    assertEquals(34, instance.height)
    assertFalse(instance.hasError)
  }

  @Test
  fun `registers optional attributes`() {
    val xmlElement = IIOMetadataNode("venue")
    xmlElement.setAttribute("building", "Building value")
    xmlElement.setAttribute("room", "Room value")
    xmlElement.setAttribute("width", "12")
    xmlElement.setAttribute("depth", "23")
    xmlElement.setAttribute("height", "34")
    xmlElement.setAttribute("circuiting", "Circuiting value")

    val instance = Venue.factory(xmlElement, null)

    assertEquals("Circuiting value", instance.circuiting)
    assertFalse(instance.hasError)
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode("venue")

    val instance = Venue.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "venue missing required building attribute",
        "venue missing required room attribute",
        "venue missing required width attribute",
        "venue missing required depth attribute",
        "venue missing required height attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode("venue")
    xmlElement.setAttribute("building", "Building value")
    xmlElement.setAttribute("room", "Room value")
    xmlElement.setAttribute("width", "bogus.1")
    xmlElement.setAttribute("depth", "bogus.2")
    xmlElement.setAttribute("height", "bogus.3")

    val instance = Venue.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "venue unable to read positive integer from width attribute",
        "venue unable to read positive integer from depth attribute",
        "venue unable to read positive integer from height attribute",
      )
    }.assertAll()
  }


}
