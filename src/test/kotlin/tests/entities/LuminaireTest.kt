package tests.entities

import CreateWithXmlElement
import Xml
import XmlElemental
import entities.Luminaire
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class LuminaireTest {

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
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

  @org.junit.Test
  fun `companion has tag`() {
    assertEquals("luminaire", Luminaire.Tag )
  }

  @org.junit.Test
  fun `has required attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("address", "123")

    val instance = Luminaire.factory(xmlElement)

    assertEquals("Type value", instance.type)
    assertEquals(123, instance.address)
    assertFalse(instance.hasError)
  }

  @org.junit.Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Luminaire.factory(xmlElement)

    assertTrue(instance.hasError)
    assertEquals("Missing required type attribute", instance.errors[0])
    assertEquals("Missing required address attribute", instance.errors[1])
    assertEquals(2, instance.errors.size)
  }

  @org.junit.Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("address", "bogus.1")

    val instance = Luminaire.factory(xmlElement)

    assertTrue(instance.hasError)
    assertEquals("Unable to read positive integer from address attribute", instance.errors[0])
    assertEquals(1, instance.errors.size)
  }

  @Test
  fun `change in address updates xmlElement and saves file`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("address", "124")
    val instance = Luminaire.factory(xmlElement)
    mockkObject(Xml)
    every { Xml.write() } returns Unit

    instance.address = 421

    assertEquals("421", xmlElement.getAttribute("address"))
    verify { Xml.write() }
  }

}
