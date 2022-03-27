package tests.entities

import CreateWithXmlElement
import Xml
import XmlElemental
import entities.Luminaire
import entities.Venue
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.assertj.core.api.SoftAssertions
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

  @Test
  fun `companion has tag`() {
    assertEquals("luminaire", Luminaire.Tag )
  }

  @Test
  fun `has required attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("location", "17.6")
    xmlElement.setAttribute("owner", "Owner name")
    xmlElement.setAttribute("address", "123")

    val instance = Luminaire.factory(xmlElement)

    SoftAssertions().apply {
      assertThat(instance.type).isEqualTo("Type value")
      assertThat(instance.location).isEqualTo(17.6f)
      assertThat(instance.owner).isEqualTo("Owner name")
      assertThat(instance.address).isEqualTo(123)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `registers optional attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("location", "17.6")
    xmlElement.setAttribute("owner", "Owner name")
    xmlElement.setAttribute("circuit", "circuit id")
    xmlElement.setAttribute("dimmer", "512")
    xmlElement.setAttribute("channel", "99")
    xmlElement.setAttribute("color", "Color name")
    xmlElement.setAttribute("target", "Zone name")
    xmlElement.setAttribute("address", "123")

    val instance = Luminaire.factory(xmlElement)

    SoftAssertions().apply {
      assertThat(instance.dimmer).isEqualTo(512)
      assertThat(instance.channel).isEqualTo(99)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Luminaire.factory(xmlElement)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required type attribute",
        "Missing required location attribute",
        "Missing required address attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("location", "bogus17.6")
    xmlElement.setAttribute("owner", "Owner name")
    xmlElement.setAttribute("dimmer", "bogus512")
    xmlElement.setAttribute("channel", "bogus99")
    xmlElement.setAttribute("address", "bogus.1")

    val instance = Luminaire.factory(xmlElement)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read floating-point number from location attribute",
        "Unable to read positive integer from dimmer attribute",
        "Unable to read positive integer from channel attribute",
        "Unable to read positive integer from address attribute",
      )
    }.assertAll()
  }

  @Test
  fun `change in address updates xmlElement and saves file`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("location", "17.6")
    xmlElement.setAttribute("owner", "Owner name")
    xmlElement.setAttribute("address", "124")
    val instance = Luminaire.factory(xmlElement)
    mockkObject(Xml)
    every { Xml.write() } returns Unit

    instance.address = 421

    assertEquals("421", xmlElement.getAttribute("address"))
    verify { Xml.write() }
  }

}
