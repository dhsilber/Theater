package tests.entities

import CreateWithXmlElement
import entities.Venue
import XmlElemental
import entities.Proscenium
import org.junit.Test
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class VenueTest {

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val luminaire = Venue.factory(xmlElement)

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
  fun `has required attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("building", "Building value")
    xmlElement.setAttribute("room", "Room value")
    xmlElement.setAttribute("width", "12")
    xmlElement.setAttribute("depth", "23")
    xmlElement.setAttribute("height", "34")

    val instance = Venue.factory(xmlElement)

    assertEquals("Building value", instance.building)
    assertEquals("Room value", instance.room)
    assertEquals(12, instance.width)
    assertEquals(23, instance.depth)
    assertEquals(34, instance.height)
    assertFalse(instance.hasError)
  }

  @Test
  fun `registers optional attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("building", "Building value")
    xmlElement.setAttribute("room", "Room value")
    xmlElement.setAttribute("width", "12")
    xmlElement.setAttribute("depth", "23")
    xmlElement.setAttribute("height", "34")
    xmlElement.setAttribute("circuiting", "Circuiting value")

    val instance = Venue.factory(xmlElement)

    assertEquals("Circuiting value", instance.circuiting)
    assertFalse(instance.hasError)
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Venue.factory(xmlElement)

    assertTrue(instance.hasError)
    assertEquals(5, instance.errors.size)
    assertEquals("Missing required building attribute", instance.errors[0])
    assertEquals("Missing required room attribute", instance.errors[1])
    assertEquals("Missing required width attribute", instance.errors[2])
    assertEquals("Missing required depth attribute", instance.errors[3])
    assertEquals("Missing required height attribute", instance.errors[4])
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("building", "Building value")
    xmlElement.setAttribute("room", "Room value")
    xmlElement.setAttribute("width", "bogus.1")
    xmlElement.setAttribute("depth", "bogus.2")
    xmlElement.setAttribute("height", "bogus.3")

    val instance = Venue.factory(xmlElement)

    assertTrue(instance.hasError)
    assertEquals("Unable to read positive integer from width attribute", instance.errors[0])
    assertEquals("Unable to read positive integer from depth attribute", instance.errors[1])
    assertEquals("Unable to read positive integer from height attribute", instance.errors[2])
    assertEquals(3, instance.errors.size)
  }


}
