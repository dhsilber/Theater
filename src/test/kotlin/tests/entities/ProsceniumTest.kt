package tests.entities

import CreateWithXmlElement
import XmlElemental
import entities.Proscenium
import org.junit.Test
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ProsceniumTest {

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val luminaire = Proscenium.factory(xmlElement)

    assertIs<XmlElemental>(luminaire)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Proscenium>>(Proscenium)
  }

  @Test
  fun `companion has tag`() {
    assertEquals("proscenium", Proscenium.Tag )
  }

  @Test
  fun `has required attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x", "1.2")
    xmlElement.setAttribute("y", "2.3")
    xmlElement.setAttribute("z", "3.4")
    xmlElement.setAttribute("height", "4.5")
    xmlElement.setAttribute("width", "5.6")
    xmlElement.setAttribute("depth", "6.7")

    val instance = Proscenium.factory(xmlElement)

    assertEquals(1.2f, instance.x)
    assertEquals(2.3f, instance.y)
    assertEquals(3.4f, instance.z)
    assertEquals(4.5f, instance.height)
    assertEquals(5.6f, instance.width)
    assertEquals(6.7f, instance.depth)
    assertFalse(instance.hasError)
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Proscenium.factory(xmlElement)

    assertTrue(instance.hasError)
    assertEquals(6, instance.errors.size)
    assertEquals("Missing required x attribute", instance.errors[0])
    assertEquals("Missing required y attribute", instance.errors[1])
    assertEquals("Missing required z attribute", instance.errors[2])
    assertEquals("Missing required height attribute", instance.errors[3])
    assertEquals("Missing required width attribute", instance.errors[4])
    assertEquals("Missing required depth attribute", instance.errors[5])
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x", "bogus 1.2")
    xmlElement.setAttribute("y", "bogus 2.3")
    xmlElement.setAttribute("z", "bogus 3.4")
    xmlElement.setAttribute("height", "bogus 4.5")
    xmlElement.setAttribute("width", "-5.6")
    xmlElement.setAttribute("depth", "bogus 6.7")

    val instance = Proscenium.factory(xmlElement)

    assertTrue(instance.hasError)
    assertEquals("Unable to read floating-point number from x attribute", instance.errors[0])
    assertEquals("Unable to read floating-point number from y attribute", instance.errors[1])
    assertEquals("Unable to read floating-point number from z attribute", instance.errors[2])
    assertEquals("Unable to read positive floating-point number from height attribute", instance.errors[3])
    assertEquals("Unable to read positive floating-point number from width attribute", instance.errors[4])
    assertEquals("Unable to read positive floating-point number from depth attribute", instance.errors[5])
    assertEquals(6, instance.errors.size)
  }

}
