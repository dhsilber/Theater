package tests.entities

import CreateWithXmlElement
import Point
import XmlElemental
import entities.Wall
import org.junit.Test
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class WallTest {

  @Test
  fun `is elemental`() {
    val xmlElement = IIOMetadataNode()

    val wall = Wall.factory(xmlElement, null)

    assertIs<XmlElemental>(wall)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Wall>>(Wall)
  }

  @Test
  fun `companion has tag`() {
    assertEquals("wall", Wall.Tag )
  }

  @Test
  fun `has required attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x1", "0.1")
    xmlElement.setAttribute("y1", "0.2")
    xmlElement.setAttribute("x2", "0.3")
    xmlElement.setAttribute("y2", "0.4")

    val instance = Wall.factory(xmlElement, null)

    assertEquals(Point(0.1F,0.2f,0f), instance.start)
    assertEquals(Point(0.3F,0.4f,0f), instance.end)
    assertFalse(instance.hasError)
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Wall.factory(xmlElement, null)

    assertTrue(instance.hasError)
    assertEquals("Missing required x1 attribute", instance.errors[0])
    assertEquals("Missing required y1 attribute", instance.errors[1])
    assertEquals("Missing required x2 attribute", instance.errors[2])
    assertEquals("Missing required y2 attribute", instance.errors[3])
    assertEquals(4, instance.errors.size)
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x1", "bogus.1")
    xmlElement.setAttribute("y1", "bogus.2")
    xmlElement.setAttribute("x2", "bogus.3")
    xmlElement.setAttribute("y2", "bogus.4")

    val instance = Wall.factory(xmlElement, null)

    assertTrue(instance.hasError)
    assertEquals("Unable to read floating-point number from x1 attribute", instance.errors[0])
    assertEquals("Unable to read floating-point number from y1 attribute", instance.errors[1])
    assertEquals("Unable to read floating-point number from x2 attribute", instance.errors[2])
    assertEquals("Unable to read floating-point number from y2 attribute", instance.errors[3])
    assertEquals(4, instance.errors.size)
  }


}