package tests

import CreateWithXmlElement
import XmlElemental
import com.mobiletheatertech.plot.Wall
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
    val wall = Wall.factory(xmlElement)
    assertIs<XmlElemental>(wall)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Wall>>(Wall)
  }

  @Test
  fun `has required attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x1", "0.1")
    xmlElement.setAttribute("y1", "0.2")
    xmlElement.setAttribute("x2", "0.3")
    xmlElement.setAttribute("y2", "0.4")

    val instance = Wall.factory(xmlElement)

    assertFalse(instance.hasError)
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Wall.factory(xmlElement)

    assertTrue(instance.hasError)
    assertEquals(4, instance.errors.size)
    assertEquals("Missing required x1 attribute", instance.errors[0])
    assertEquals("Missing required y1 attribute", instance.errors[1])
    assertEquals("Missing required x2 attribute", instance.errors[2])
    assertEquals("Missing required y2 attribute", instance.errors[3])
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x1", "bogus.1")
    xmlElement.setAttribute("y1", "bogus.2")
    xmlElement.setAttribute("x2", "bogus.3")
    xmlElement.setAttribute("y2", "bogus.4")

    val instance = Wall.factory(xmlElement)

    assertTrue(instance.hasError)
    assertEquals(4, instance.errors.size)
    assertEquals("Unable to read floating-point number from x1 attribute", instance.errors[0])
    assertEquals("Unable to read floating-point number from y1 attribute", instance.errors[1])
    assertEquals("Unable to read floating-point number from x2 attribute", instance.errors[2])
    assertEquals("Unable to read floating-point number from y2 attribute", instance.errors[3])
  }


}