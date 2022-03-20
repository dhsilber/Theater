package tests

import CreateWithXmlElement
import XmlElemental
import com.mobiletheatertech.plot.Luminaire
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.*

class LuminaireTest {

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml.Companion)
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()
    val luminaire = Luminaire.factory(xmlElement)
    assertIs<XmlElemental>(luminaire)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Luminaire>>(Luminaire)
  }

  @Test
  fun `must have type attribute`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("address", "123")
    val instance = Luminaire.factory(xmlElement)

    assertEquals("Type value", instance.type)
    assertFalse(instance.hasError)
  }

  @Test
  fun `set error state when no type attribute`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("address", "123")
    val instance = Luminaire.factory(xmlElement)

    assertTrue(instance.hasError)
    assertEquals(1, instance.errors.size)
    assertEquals("Missing required type attribute", instance.errors[0])
  }

  @Test
  fun `must have address attribute`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("address", "123")
    val instance = Luminaire.factory(xmlElement)

    assertEquals(123, instance.address)
    assertFalse(instance.hasError)
  }

  @Test
  fun `set error state when no address attribute`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    val instance = Luminaire.factory(xmlElement)

    assertTrue(instance.hasError)
    assertEquals(1, instance.errors.size)
    assertEquals("Missing required address attribute", instance.errors[0])
  }

  @Test
  fun `set error state when address attribute is not a number`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("address", "address value")
    val instance = Luminaire.factory(xmlElement)

    assertTrue(instance.hasError)
    assertEquals(1, instance.errors.size)
    assertEquals("Unable to read number from address attribute", instance.errors[0])
  }

  @Test
  fun `change in address updates xmlElement and saves file`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("address", "124")
    val instance = Luminaire.factory(xmlElement)
    mockkObject(Xml.Companion)
    every { Xml.Companion.write() } returns Unit

    instance.address = 421

    assertEquals("421", xmlElement.getAttribute("address"))
    verify { Xml.Companion.write() }
  }

}
